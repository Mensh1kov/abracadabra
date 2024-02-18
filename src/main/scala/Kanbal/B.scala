package Kanbal
import java.io.PrintWriter
import scala.annotation.tailrec
import scala.io.Source

object B extends App {
  type Vertex = Int
  val Start = Int.MinValue
  val End   = Int.MaxValue

  class Graph(adjacencyLists: Map[Vertex, List[Vertex]]) {
    def size: Int                                  = adjacencyLists.size
    def getNeighbors(vertex: Vertex): List[Vertex] = adjacencyLists.getOrElse(vertex, Nil)

    def addEdge(from: Vertex, to: Vertex): Graph =
      new Graph(adjacencyLists + (from -> (to :: adjacencyLists.getOrElse(from, Nil))))

    def remEdge(from: Vertex, to: Vertex): Graph =
      new Graph(adjacencyLists + (from -> adjacencyLists.getOrElse(from, Nil).filter(_ != to)))

    def addNeighbors(from: Vertex, neighbors: List[Vertex]): Graph =
      new Graph(adjacencyLists + (from -> (neighbors ++ adjacencyLists.getOrElse(from, Nil))))

  }

  def fordFulkerson(graph: Graph): Map[(Vertex, Vertex), Int] = {
    @tailrec
    def loop(graph: Graph, acc: Map[(Vertex, Vertex), Int]): Map[(Vertex, Vertex), Int]  =
      dfs(graph, Start, End).sliding(2, 1).toList match {
        case Nil  => acc
        case path => {
          val (g, a) = path.foldLeft((graph, acc)) { case ((g, a), x :: y :: _) =>

            val max_v = if (x != Start && y != Start) Math.max(x, y) else x
            val min_v = if (x != End && y != End) Math.min(x, y) else y
            (g.remEdge(x, y).addEdge(y, x), a + ((max_v, min_v) -> (1 + a.getOrElse((max_v, min_v), 0))))
          }
          loop(g, a)
        }
      }
    loop(graph, Map.empty)
  }

  def dfs(graph: Graph, from: Vertex, to: Vertex): List[Vertex] = {
    def extractPath(from: Vertex, to: Vertex, prior: Map[Vertex, Vertex]): List[Vertex] = {
      @tailrec
      def loop(acc: List[Vertex]): List[Vertex] = acc match {
        case head :: _ if head == from               => acc
        case head :: _ if prior.getOrElse(head, 0) == 0 => Nil
        case head :: _                                  => loop(prior.getOrElse(head, 0) :: acc)
      }
      loop(List(to))
    }

    @tailrec
    def loop(stack: List[Vertex], visited: Set[Vertex], prior: Map[Vertex, Vertex]): List[Vertex] =
      stack match {
        case head :: _ if head == to => extractPath(from, to, prior)
        case head :: tail            =>
          val neibghors = graph.getNeighbors(head).toSet.diff(visited).toList
          loop(
            neibghors ++ tail,
            visited + head,
            neibghors.foldLeft(prior) { case (p, v) => p + (v -> head) }
          )
        case _                       => Nil
      }
    loop(List(from), Set(), Map(from -> 0))
  }

  def input: (Int, Graph) = {
    val iter          = Source.fromFile(
      "in.txt"
    ).getLines()
    val (x :: y :: _) = iter.next().split(" ").map(_.toInt).toList
    val graph         = (1 to x).foldLeft(new Graph(Map.empty)) {
      case (g, v) => g.addEdge(Start, v).addNeighbors(
          v,
          iter.next().split(" ").map(-_.toInt).filter(_ != 0).toList
        )
    }
    (x, (1 to y).foldLeft(graph) {
      case (g, v) => g.addEdge(-v, End)
    })
  }


  val (x, g) = input
  val foo = fordFulkerson(g)
  val par = foo.keys.toList
  val res    = (1 to x).foldLeft(List.empty[Int]) { case (l, x) => l :+ par.find { case p@(a, b) => a == x && foo.getOrElse(p, 0) % 2 == 1}.map(-_._2).getOrElse(0) }.mkString(" ")
  val writer = new PrintWriter("out.txt")
  writer.write(res)
  writer.close()
}
