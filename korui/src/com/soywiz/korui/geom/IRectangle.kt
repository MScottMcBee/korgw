package com.soywiz.korui.geom

data class IRectangle(var x: Int = 0, var y: Int = 0, var width: Int = 0, var height: Int = 0) {
	fun set(that: IRectangle) = set(that.x, that.y, that.width, that.height)

	var left: Int set(value) = run { x = value }; get() = x
	var top: Int set(value) = run { y = value }; get() = y

	var right: Int set(value) = run { width = value - x }; get() = x + width
	var bottom: Int set(value) = run { height = value - y }; get() = y + height

	fun setBounds(left: Int, top: Int, right: Int, bottom: Int) = set(left, top, right - left, bottom - top)

	fun set(x: Int, y: Int, width: Int, height: Int) = this.apply {
		this.x = x
		this.y = y
		this.width = width
		this.height = height
	}

	fun setPosition(x: Int, y: Int) = this.apply {
		this.x = x
		this.y = y
	}

	fun setSize(width: Int, height: Int) = this.apply {
		this.width = width
		this.height = height
	}
}
