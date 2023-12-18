package adventOfCode.util

typealias PairOf<T> = Pair<T, T>
typealias TripleOf<T> = Triple<T, T, T>
typealias Point2D = PairOf<Int>
typealias Point3D = TripleOf<Int>

operator fun Point2D.unaryMinus() = Point2D(-this.first, -this.second)

operator fun Point2D.plus(other: Point2D) = this.first + other.first to this.second + other.second
operator fun Point2D.plus(n: Int) = this.first + n to this.second + n
operator fun Int.plus(point: Point2D) = this + point.first to this + point.second

operator fun Point2D.minus(other: Point2D) = this.first - other.first to this.second - other.second
operator fun Point2D.minus(n: Int) = this.first - n to this.second - n
operator fun Int.minus(point: Point2D) = this - point.first to this - point.second

operator fun Point2D.times(other: Point2D) = this.first * other.first to this.second * other.second
operator fun Point2D.times(n: Int) = this.first * n to this.second * n
operator fun Int.times(point: Point2D) = this * point.first to this * point.second

operator fun Point3D.plus(other: Point3D) = Point3D(
    this.first + other.first,
    this.second + other.second,
    this.third + other.third
)
