package com.soywiz.korev

import com.soywiz.kds.*
import com.soywiz.klock.*
import com.soywiz.korio.file.*
import com.soywiz.korio.lang.*
import com.soywiz.korma.geom.*

data class MouseEvent(
    var type: Type = Type.MOVE,
    var id: Int = 0,
    var x: Int = 0,
    var y: Int = 0,
    var button: MouseButton = MouseButton.LEFT,
    var buttons: Int = 0,
    var scrollDeltaX: Double = 0.0,
    var scrollDeltaY: Double = 0.0,
    var scrollDeltaZ: Double = 0.0,
    var isShiftDown: Boolean = false,
    var isCtrlDown: Boolean = false,
    var isAltDown: Boolean = false,
    var isMetaDown: Boolean = false,
    var scaleCoords: Boolean = true
) :
	Event() {
	enum class Type { MOVE, DRAG, UP, DOWN, CLICK, ENTER, EXIT, SCROLL }
}

data class Touch(
	val index: Int = -1,
	var active: Boolean = false,
	var id: Int = -1,
	var startTime: DateTime = DateTime.EPOCH,
	var currentTime: DateTime = DateTime.EPOCH,
	var start: Point = Point(),
	var current: Point = Point()
) : Extra by Extra.Mixin() {
	companion object {
		val dummy = Touch(-1)
	}
}

data class TouchEvent(
    var type: Type = Type.START,
    var screen: Int = 0,
    var touch: Touch = Touch(),
    var scaleCoords: Boolean = true
) : Event() {
	enum class Type { START, END, MOVE }
}

data class KeyEvent(
    var type: Type = Type.UP,
    var id: Int = 0,
    var key: Key = Key.UP,
    var keyCode: Int = 0,
    var char: Char = '\u0000'
) : Event() {
	enum class Type { UP, DOWN, TYPE }
}

data class GamePadConnectionEvent(var type: Type = Type.CONNECTED, var gamepad: Int = 0) : Event() {
	enum class Type { CONNECTED, DISCONNECTED }
}

data class GamePadButtonEvent(var type: Type, var gamepad: Int, var button: GameButton, var value: Double) : Event() {
	enum class Type { UP, DOWN }
}

data class GamePadStickEvent(var gamepad: Int, var stick: GameStick, var x: Double, var y: Double) : Event()

data class ChangeEvent(var oldValue: Any? = null, var newValue: Any? = null) : Event()

data class ReshapeEvent(var x: Int = 0, var y: Int = 0, var width: Int = 0, var height: Int = 0) : Event()

data class FullScreenEvent(var fullscreen: Boolean = false) : Event()

class RenderEvent() : Event()

class InitEvent() : Event()

class DisposeEvent() : Event()

data class DropFileEvent(var type: Type = Type.ENTER, var files: List<VfsFile>? = null) : Event() {
	enum class Type { ENTER, EXIT, DROP }
}

class MouseEvents(val ed: EventDispatcher) : Closeable {
	fun click(callback: () -> Unit) =
		ed.addEventListener<MouseEvent> { if (it.type == MouseEvent.Type.CLICK) callback() }

	fun up(callback: () -> Unit) = ed.addEventListener<MouseEvent> { if (it.type == MouseEvent.Type.UP) callback() }
	fun down(callback: () -> Unit) = ed.addEventListener<MouseEvent> { if (it.type == MouseEvent.Type.DOWN) callback() }
	fun move(callback: () -> Unit) = ed.addEventListener<MouseEvent> { if (it.type == MouseEvent.Type.MOVE) callback() }
	fun drag(callback: () -> Unit) = ed.addEventListener<MouseEvent> { if (it.type == MouseEvent.Type.DRAG) callback() }
	fun enter(callback: () -> Unit) =
		ed.addEventListener<MouseEvent> { if (it.type == MouseEvent.Type.ENTER) callback() }

	fun exit(callback: () -> Unit) = ed.addEventListener<MouseEvent> { if (it.type == MouseEvent.Type.EXIT) callback() }
	override fun close() {
	}
}

class KeysEvents(val ed: EventDispatcher) : Closeable {
	fun down(callback: KeyEvent.() -> Unit) =
		ed.addEventListener<KeyEvent> { if (it.type == KeyEvent.Type.DOWN) callback(it) }

	fun up(callback: KeyEvent.() -> Unit) =
		ed.addEventListener<KeyEvent> { if (it.type == KeyEvent.Type.UP) callback(it) }

	fun press(callback: KeyEvent.() -> Unit) =
		ed.addEventListener<KeyEvent> { if (it.type == KeyEvent.Type.TYPE) callback(it) }

	fun down(key: Key, callback: KeyEvent.() -> Unit) =
		ed.addEventListener<KeyEvent> { if (it.type == KeyEvent.Type.DOWN && it.key == key) callback(it) }

	fun up(key: Key, callback: KeyEvent.() -> Unit) =
		ed.addEventListener<KeyEvent> { if (it.type == KeyEvent.Type.UP && it.key == key) callback(it) }

	fun press(key: Key, callback: KeyEvent.() -> Unit) =
		ed.addEventListener<KeyEvent> { if (it.type == KeyEvent.Type.TYPE && it.key == key) callback(it) }

	override fun close() {
	}
}

fun EventDispatcher.mouse(callback: MouseEvents.() -> Unit) = MouseEvents(this).apply(callback)
fun EventDispatcher.keys(callback: KeysEvents.() -> Unit) = KeysEvents(this).apply(callback)
