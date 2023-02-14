package edu.msudenver.kotlinlogin

class Bike(
    var year: String= "",
    var make: String = "",
    var model: String = "",
    var size: String = "") {
    override fun toString(): String {
        return "Bike(year='$year', make='$make', model='$model', size='$size')"
    }
}


