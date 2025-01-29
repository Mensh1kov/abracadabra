object FilterType extends App {
  trait Account

  trait Debit
  trait Credit
  trait Saving

  case object DebitAccount extends Account with Debit
  case object CreditAccount extends Account with Credit
  case object SavingAccount extends Account with Saving

  trait TypeFilter extends Debit with Credit

  def foo(a: Account with TypeFilter) = ()

//  foo(DebitAccount) // error

}
