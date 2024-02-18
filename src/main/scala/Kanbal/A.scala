package Kanbal

import java.io.PrintWriter
import scala.collection.immutable.HashMap
import scala.io.{Source, StdIn}
import scala.util.matching.Regex

object A extends App {
  val pattern: Regex = raw"\d+".r

  case class Skeleton(adjacencyLists: HashMap[Int, List[Int]], weight: Int) {

    def addEdge(parent: Int, neighbour: Int, wight: Int): Skeleton = this.copy(
      adjacencyLists + (
        parent    -> { neighbour :: adjacencyLists.getOrElse(parent, Nil) },
        neighbour -> { parent :: adjacencyLists.getOrElse(neighbour, Nil) }
      ),
      wight + this.weight
    )

    override def toString: String = adjacencyLists.toList.sortBy(_._1).foldLeft("") {
      case (acc, (_, neighbours)) => acc + (neighbours.sorted :+ 0).mkString(" ") + "\n"
    } + weight

  }

  case class Graph(adjacencyArray: Array[Int]) {
    def numberOfVertices: Int = Math.max(adjacencyArray(1) - 2, 1)

    def getAdjacentEdges(vertex: Int): Array[(Int, Int)] = {
      val start = adjacencyArray(vertex)
      if (start == 0) Array.empty
      else adjacencyArray.slice(
        start,
        adjacencyArray(vertex + 1)
      ).grouped(2).map(pair => (pair(0), pair(1))).toArray
    }

  }

  def adjacencyArray: Array[Int] = Source.fromFile("in.txt").getLines().flatMap(
    pattern.findAllIn
  ).takeWhile(_ != "32767").map(_.toInt).toArray

  def JarnikPrimaDijkstra(graph: Graph): Skeleton = {
    @scala.annotation.tailrec
    def loop(skeleton: Skeleton): Skeleton =
      if (graph.numberOfVertices <= skeleton.adjacencyLists.size) skeleton
      else {
        val (parent, (neighbour, wight)) =
          skeleton.adjacencyLists.keySet.toList.flatMap(vertex =>
            graph.getAdjacentEdges(vertex).map((vertex, _))
          ).filter { case (_, (neighbour, _)) =>
            !skeleton.adjacencyLists.contains(neighbour)
          }.minBy(_._2._2)
        loop(skeleton.addEdge(parent, neighbour, wight))
      }
    loop(Skeleton(HashMap(1 -> List.empty), 0))
  }

  val res = JarnikPrimaDijkstra(Graph(adjacencyArray)).toString
  val writer = new PrintWriter("out.txt")
  writer.write(res)
  writer.close()
}
