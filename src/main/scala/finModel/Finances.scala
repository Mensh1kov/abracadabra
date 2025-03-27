package finModel

import cats.syntax.all._

import cats.Eval
import cats.data.State

object Finances extends App {
  sealed trait Account
  case class Credit(rate: Double, amount: Double, duration: Double) extends Account
  case class Saving(rate: Double, amount: Double, capitalization: Boolean) extends Account
  case class Debit(amount: Double) extends Account

  case class MyFin(debit: Debit, credit: Credit, saving: Saving)

  case class Stats(p: Double)

  // https://mortgage-calculator.ru/%D1%84%D0%BE%D1%80%D0%BC%D1%83%D0%BB%D0%B0-%D1%80%D0%B0%D1%81%D1%87%D0%B5%D1%82%D0%B0-%D0%B8%D0%BF%D0%BE%D1%82%D0%B5%D0%BA%D0%B8/

  def makeDebitState(profit: Double): State[Debit, Double] = State { case debit @ Debit(amount) =>
    (debit.copy(amount = 0), profit + amount)
  }

  val creditState: State[Credit, Double] = State { case credit @ Credit(rate, amount, duration) =>
    val monthRate = rate / 12
    val commonRate = Math.pow(1 + monthRate, duration)
    val percents = amount * monthRate
    val payment = amount * monthRate * commonRate / (commonRate - 1)
    val afterPayment = credit.copy(amount = amount - (payment - percents), duration = duration - 1)
    (afterPayment, -payment)
  }

  val savingState: State[Saving, Double] = State { case saving @ Saving(rate, amount, capitalization) =>
    val monthRate = rate / 12
    val percent = monthRate * amount

    if (capitalization) {
      val afterCapitalization = saving.copy(amount = amount + percent)
      (afterCapitalization, 0)
    } else (saving, percent)
  }

  val ipoteka = Credit(0.05, 6_200_600, 30 * 12)
  val saving = Saving(0.17, 700000, false)
  val debit = Debit(0)
  val myFin = MyFin(debit, ipoteka, saving)
  val zp = 190_000
  val expenses = -50_000

  val debitState = makeDebitState(zp + expenses)
  val duration = 21

  val myFinState: State[MyFin, Double] = State { case MyFin(debit, credit, saving) =>
    {
      for {
        (nextDebit, amount) <- debitState.run(debit)
        (nextCredit, pyment) <- creditState.run(credit)
        (nextSaving, percent) <- savingState.run(saving)
        profit = amount + pyment + percent
      } yield (MyFin(nextDebit, nextCredit, nextSaving), profit)
    }.value
  }

  val allMoneyToIpotekaState: State[MyFin, Stats] = State(myFin =>
    {
      for {
        (nextMyFin @ MyFin(_, nextCredit, _), amount) <- myFinState.run(myFin)
        updatedNextCredit = nextCredit.copy(amount = nextCredit.amount - amount)
      } yield (nextMyFin.copy(credit = updatedNextCredit), Stats(-amount))
    }.value
  )

  val allMoneyToSaving: State[MyFin, Stats] = State(myFin =>
    {
      for {
        (nextMyFin @ MyFin(_, _, nextSaving), amount) <- myFinState.run(myFin)
        updatedNextSaving = nextSaving.copy(amount = nextSaving.amount + amount)
      } yield (nextMyFin.copy(saving = updatedNextSaving), Stats(0))
    }.value
  )

  val strategy1 = (1 to duration).foldLeft((ipoteka, saving, Stats(0))) {
    case ((ipoteka, saving, stats), _) => {
      val (nextIpoteka, payment) = creditState.run(ipoteka).value
      val (nextSaving, percent) = savingState.run(saving).value

      val updatedStats = stats.copy(p = stats.p + payment + expenses)
      val amount = zp + expenses + payment + percent

      if (amount < 0) throw new Exception("Закончились деньги!")

      val updatedSaving = nextSaving.copy(amount = nextSaving.amount + amount)
      (nextIpoteka, updatedSaving, updatedStats)
    }
  }

  def run(myFin: MyFin, strategy: State[MyFin, Stats], duration: Int): (MyFin, Stats) =
    (1 to duration).foldLeft((myFin, Stats(0))) { case ((myFin, accStats), _) =>
      {
        for {
          (nextMyFin, stats) <- strategy.run(myFin)
          updatedStats = accStats.copy(p = accStats.p + stats.p)
        } yield (nextMyFin, updatedStats)
      }.value
    }

  val strategy2 = (1 to duration).foldLeft((ipoteka, saving, Stats(0))) {
    case ((ipoteka, saving, stats), _) => {
      val (nextIpoteka, payment) = creditState.run(ipoteka).value
      val (nextSaving, percent) = savingState.run(saving).value

      val amount = zp + expenses + payment + percent
      val updatedStats = stats.copy(p = stats.p + payment + expenses - amount)

      if (amount < 0) throw new Exception("Закончились деньги!")

      val updatedIpoteka = nextIpoteka.copy(amount = nextIpoteka.amount - amount)
      (updatedIpoteka, nextSaving, updatedStats)
    }
  }

  val strategy11 = run(myFin, allMoneyToSaving, duration)
  val strategy22 = run(myFin, allMoneyToIpotekaState, duration)

  def printStrategy2(myFin: MyFin, stats: Stats): Unit = {
    println(f"Ипотека: ${myFin.credit.amount}%.2f")
    println(f"Накопления: ${myFin.saving.amount}%.2f")
    println(f"Расходы: ${stats.p}%.2f")
    println(f"Итого (Н - И) = ${myFin.saving.amount - myFin.credit.amount}%.2f")
  }
  def printStrategy(ipoteka: Credit, saving: Saving, stats: Stats): Unit = {
    println(f"Ипотека: ${ipoteka.amount}%.2f")
    println(f"Накопления: ${saving.amount}%.2f")
    println(f"Расходы: ${stats.p}%.2f")
    println(f"Итого (Н - И) = ${saving.amount - ipoteka.amount}%.2f")
  }

  println(s"Период $duration месяца\n")
  val (credit1, saving1, stat1) = strategy1
////  val (myFin11, stats11) = strategy11
//  println(myFin11 == MyFin(Debit(0), credit1, saving1))
//  println(stats11 == stat1)
  println("Cтратегия 1")
  printStrategy(credit1, saving1, stat1)
  println("Cтратегия 11")
//  printStrategy2(myFin11, stats11)

  println()
  val (credit2, saving2, stat2) = strategy2
//  val (myFin22, stats22) = strategy22
//  println(myFin22 == MyFin(Debit(0), credit2, saving2))
//  println(stats22 == stat2)
  println("Cтратегия 2")
  printStrategy(credit2, saving2, stat2)
//  println("Cтратегия 22")
//  printStrategy2(myFin22, stats22)

  println()
  println(f"Разница в расходах (C1 - C2): ${stat1.p - stat2.p}%.2f")
  println(f"Вложим разницу в ипотеку: ${credit1.amount - (stat1.p - stat2.p)}%.2f")
  println(f"Остаток накоплений: ${saving1.amount - (stat1.p - stat2.p)}%.2f")
}
