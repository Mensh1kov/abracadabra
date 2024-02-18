package Leetcode

object PalindromeNumber {
  def isPalindrome(x: Int): Boolean = {
    if (x < 0) false
    else {
      val current = x.toString
      val reverse = current.reverse
      current == reverse
    }
  }
}
