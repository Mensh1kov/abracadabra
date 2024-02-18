object ApplicativeTask {
  trait Applicative1[F[_]] {
    def ap[A, B](fab: F[A => B])(fa: F[A]): F[B]
    def pure[A](a: A): F[A]
//    def product[A, B](fa: F[A], fb: F[B]): F[(A, B)] = ap(ap(pure((a: A) => (b: B) => (a, b)))(fa))(fb)
//    def product[A, B](fa: F[A], fb: F[B]): F[(A, B)] = ap(map(fa)(a => (b: B) => (a, b)))(fb)
    def map[A, B](fa: F[A])(f: A => B): F[B] = ap(pure(f))(fa)
  }

  trait Applicative2[F[_]] {
//    def ap[A, B](fab: F[A => B])(fa: F[A]): F[B] = map(product(fab, fa)) { case (f, a) => f(a) }
    def pure[A](a: A): F[A]
    def product[A, B](fa: F[A], fb: F[B]): F[(A, B)]
//    def map[A, B](fa: F[A])(f: A => B): F[B] = product(pure(f), fa)
  }
}
