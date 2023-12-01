package adventOfCode.util

typealias PairOf<T> = Pair<T, T>

typealias TripleOf<T> = Triple<T, T, T>

typealias Point2D = PairOf<Int>

operator fun Point2D.plus(other: Point2D) = Point2D(
    this.first + other.first,
    this.second + other.second
)

typealias Point3D = Triple<Int, Int, Int>

operator fun Point3D.plus(other: Point3D) = Point3D(
    this.first + other.first,
    this.second + other.second,
    this.third + other.third
)
