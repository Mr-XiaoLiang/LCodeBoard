package com.lollipop.codeboard.keyboard

import android.view.KeyEvent

object Keys {

    interface Key {
        val keyValue: String
        val keyCode: Int
    }

    val keyList = arrayOf<List<Key>>(
        Layout.entries,
        Letter.entries,
        Number.entries,
        Symbol.entries,
        Decoration.entries,
        Function.entries
    )

    fun findKey(keyValue: String): Key? {
        for (keys in keyList) {
            val value = findByKeyValue(keys, keyValue)
            if (value != null) {
                return value
            }
        }
        return null
    }

    fun findDecoration(keyValue: String): Key? {
        return findByKeyValue(Decoration.entries, keyValue)
    }

    private fun findByKeyValue(list: List<Key>, keyValue: String): Key? {
        return list.firstOrNull { it.keyValue == keyValue }
    }

    enum class Layout(
        override val keyValue: String,
        override val keyCode: Int = 0
    ) : Key {

        Empty(keyValue = ""),
        Back(keyValue = "Back")

    }

    enum class Letter(
        override val keyValue: String,
        override val keyCode: Int
    ) : Key {
        a(keyValue = "a", keyCode = KeyEvent.KEYCODE_A),
        b(keyValue = "b", keyCode = KeyEvent.KEYCODE_B),
        c(keyValue = "c", keyCode = KeyEvent.KEYCODE_C),
        d(keyValue = "d", keyCode = KeyEvent.KEYCODE_D),
        e(keyValue = "e", keyCode = KeyEvent.KEYCODE_E),
        f(keyValue = "f", keyCode = KeyEvent.KEYCODE_F),
        g(keyValue = "g", keyCode = KeyEvent.KEYCODE_G),
        h(keyValue = "h", keyCode = KeyEvent.KEYCODE_H),
        i(keyValue = "i", keyCode = KeyEvent.KEYCODE_I),
        j(keyValue = "j", keyCode = KeyEvent.KEYCODE_J),
        k(keyValue = "k", keyCode = KeyEvent.KEYCODE_K),
        l(keyValue = "l", keyCode = KeyEvent.KEYCODE_L),
        m(keyValue = "m", keyCode = KeyEvent.KEYCODE_M),
        n(keyValue = "n", keyCode = KeyEvent.KEYCODE_N),
        o(keyValue = "o", keyCode = KeyEvent.KEYCODE_O),
        p(keyValue = "p", keyCode = KeyEvent.KEYCODE_P),
        q(keyValue = "q", keyCode = KeyEvent.KEYCODE_Q),
        r(keyValue = "r", keyCode = KeyEvent.KEYCODE_R),
        s(keyValue = "s", keyCode = KeyEvent.KEYCODE_S),
        t(keyValue = "t", keyCode = KeyEvent.KEYCODE_T),
        u(keyValue = "u", keyCode = KeyEvent.KEYCODE_U),
        v(keyValue = "v", keyCode = KeyEvent.KEYCODE_V),
        w(keyValue = "w", keyCode = KeyEvent.KEYCODE_W),
        x(keyValue = "x", keyCode = KeyEvent.KEYCODE_X),
        y(keyValue = "y", keyCode = KeyEvent.KEYCODE_Y),
        z(keyValue = "z", keyCode = KeyEvent.KEYCODE_Z),
        A(keyValue = "A", keyCode = KeyEvent.KEYCODE_A),
        B(keyValue = "B", keyCode = KeyEvent.KEYCODE_B),
        C(keyValue = "C", keyCode = KeyEvent.KEYCODE_C),
        D(keyValue = "D", keyCode = KeyEvent.KEYCODE_D),
        E(keyValue = "E", keyCode = KeyEvent.KEYCODE_E),
        F(keyValue = "F", keyCode = KeyEvent.KEYCODE_F),
        G(keyValue = "G", keyCode = KeyEvent.KEYCODE_G),
        H(keyValue = "H", keyCode = KeyEvent.KEYCODE_H),
        I(keyValue = "I", keyCode = KeyEvent.KEYCODE_I),
        J(keyValue = "J", keyCode = KeyEvent.KEYCODE_J),
        K(keyValue = "K", keyCode = KeyEvent.KEYCODE_K),
        L(keyValue = "L", keyCode = KeyEvent.KEYCODE_L),
        M(keyValue = "M", keyCode = KeyEvent.KEYCODE_M),
        N(keyValue = "N", keyCode = KeyEvent.KEYCODE_N),
        O(keyValue = "O", keyCode = KeyEvent.KEYCODE_O),
        P(keyValue = "P", keyCode = KeyEvent.KEYCODE_P),
        Q(keyValue = "Q", keyCode = KeyEvent.KEYCODE_Q),
        R(keyValue = "R", keyCode = KeyEvent.KEYCODE_R),
        S(keyValue = "S", keyCode = KeyEvent.KEYCODE_S),
        T(keyValue = "T", keyCode = KeyEvent.KEYCODE_T),
        U(keyValue = "U", keyCode = KeyEvent.KEYCODE_U),
        V(keyValue = "V", keyCode = KeyEvent.KEYCODE_V),
        W(keyValue = "W", keyCode = KeyEvent.KEYCODE_W),
        X(keyValue = "X", keyCode = KeyEvent.KEYCODE_X),
        Y(keyValue = "Y", keyCode = KeyEvent.KEYCODE_Y),
        Z(keyValue = "Z", keyCode = KeyEvent.KEYCODE_Z),
    }

    enum class Number(
        override val keyValue: String,
        override val keyCode: Int
    ) : Key {
        Num1(keyValue = "1", keyCode = KeyEvent.KEYCODE_1),
        Num2(keyValue = "2", keyCode = KeyEvent.KEYCODE_2),
        Num3(keyValue = "3", keyCode = KeyEvent.KEYCODE_3),
        Num4(keyValue = "4", keyCode = KeyEvent.KEYCODE_4),
        Num5(keyValue = "5", keyCode = KeyEvent.KEYCODE_5),
        Num6(keyValue = "6", keyCode = KeyEvent.KEYCODE_6),
        Num7(keyValue = "7", keyCode = KeyEvent.KEYCODE_7),
        Num8(keyValue = "8", keyCode = KeyEvent.KEYCODE_8),
        Num9(keyValue = "9", keyCode = KeyEvent.KEYCODE_9),
        Num0(keyValue = "0", keyCode = KeyEvent.KEYCODE_0),
    }

    enum class Symbol(
        override val keyValue: String,
        override val keyCode: Int
    ) : Key {
        Exclamation(keyValue = "!", keyCode = KeyEvent.KEYCODE_1),
        At(keyValue = "@", keyCode = KeyEvent.KEYCODE_AT),
        Hash(keyValue = "#", keyCode = KeyEvent.KEYCODE_POUND),
        Dollar(keyValue = "$", keyCode = 0),
        Percent(keyValue = "%", keyCode = 0),
        Caret(keyValue = "^", keyCode = 0),
        Ampersand(keyValue = "&", keyCode = 0),
        Asterisk(keyValue = "*", keyCode = KeyEvent.KEYCODE_STAR),
        ParenthesesLeft(keyValue = "(", keyCode = KeyEvent.KEYCODE_NUMPAD_LEFT_PAREN),
        ParenthesesRight(keyValue = ")", keyCode = KeyEvent.KEYCODE_NUMPAD_RIGHT_PAREN),
        Underscore(keyValue = "_", keyCode = KeyEvent.KEYCODE_MINUS),
        Plus(keyValue = "+", keyCode = KeyEvent.KEYCODE_PLUS),
        CurlyLeft(keyValue = "{", keyCode = KeyEvent.KEYCODE_LEFT_BRACKET),
        CurlyRight(keyValue = "}", keyCode = KeyEvent.KEYCODE_RIGHT_BRACKET),
        VerticalBar(keyValue = "|", keyCode = KeyEvent.KEYCODE_BACKSLASH),
        Colon(keyValue = ":", keyCode = KeyEvent.KEYCODE_SEMICOLON),
        QuoteSingle(keyValue = "'", keyCode = KeyEvent.KEYCODE_APOSTROPHE),
        QuoteDouble(keyValue = "\"", keyCode = KeyEvent.KEYCODE_APOSTROPHE),
        Less(keyValue = "<", KeyEvent.KEYCODE_COMMA),
        Greater(keyValue = ">", keyCode = KeyEvent.KEYCODE_PERIOD),
        Question(keyValue = "?", keyCode = KeyEvent.KEYCODE_SLASH),
        SlashForward(keyValue = "/", keyCode = KeyEvent.KEYCODE_SLASH),
        SlashBack(keyValue = "\\", keyCode = KeyEvent.KEYCODE_BACKSLASH),
        Tilde(keyValue = "~", keyCode = KeyEvent.KEYCODE_GRAVE),
        QuoteBack(keyValue = "`", keyCode = KeyEvent.KEYCODE_GRAVE),
        Comma(keyValue = ",", keyCode = KeyEvent.KEYCODE_COMMA),
        Dot(keyValue = ".", keyCode = KeyEvent.KEYCODE_PERIOD),
        Semicolon(keyValue = ";", keyCode = KeyEvent.KEYCODE_SEMICOLON),
        Hyphen(keyValue = "-", keyCode = KeyEvent.KEYCODE_MINUS),
        Equals(keyValue = "=", keyCode = KeyEvent.KEYCODE_EQUALS),
        SquareLeft(keyValue = "[", keyCode = KeyEvent.KEYCODE_LEFT_BRACKET),
        SquareRight(keyValue = "]", keyCode = KeyEvent.KEYCODE_RIGHT_BRACKET),
    }

    enum class Decoration(
        override val keyValue: String,
        override val keyCode: Int
    ) : Key {
        Shift(keyValue = "Shift", keyCode = KeyEvent.KEYCODE_SHIFT_LEFT),
        Command(keyValue = "Command", keyCode = KeyEvent.KEYCODE_CTRL_LEFT),
        Option(keyValue = "Option", keyCode = KeyEvent.KEYCODE_ALT_RIGHT),
        Backspace(keyValue = "Backspace", keyCode = KeyEvent.KEYCODE_DEL),
        Enter(keyValue = "Enter", keyCode = KeyEvent.KEYCODE_ENTER),
        Space(keyValue = "Space", keyCode = KeyEvent.KEYCODE_SPACE),
        Delete(keyValue = "Delete", keyCode = KeyEvent.KEYCODE_FORWARD_DEL),
        Tab(keyValue = "Tab", keyCode = KeyEvent.KEYCODE_TAB),
        Escape(keyValue = "Escape", keyCode = KeyEvent.KEYCODE_ESCAPE),
        CapsLock(keyValue = "CapsLock", keyCode = KeyEvent.KEYCODE_CAPS_LOCK),
        ArrowUp(keyValue = "ArrowUp", keyCode = KeyEvent.KEYCODE_DPAD_UP),
        ArrowDown(keyValue = "ArrowDown", keyCode = KeyEvent.KEYCODE_DPAD_DOWN),
        ArrowLeft(keyValue = "ArrowLeft", keyCode = KeyEvent.KEYCODE_DPAD_LEFT),
        ArrowRight(keyValue = "ArrowRight", keyCode = KeyEvent.KEYCODE_DPAD_RIGHT),
        Symbol(keyValue = "Symbol", keyCode = 0),
        Language(keyValue = "Language", keyCode = KeyEvent.KEYCODE_LANGUAGE_SWITCH)
    }

    enum class Function(
        override val keyValue: String,
        override val keyCode: Int
    ) : Key {
        F1(keyValue = "F1", keyCode = KeyEvent.KEYCODE_F1),
        F2(keyValue = "F2", keyCode = KeyEvent.KEYCODE_F2),
        F3(keyValue = "F3", keyCode = KeyEvent.KEYCODE_F3),
        F4(keyValue = "F4", keyCode = KeyEvent.KEYCODE_F4),
        F5(keyValue = "F5", keyCode = KeyEvent.KEYCODE_F5),
        F6(keyValue = "F6", keyCode = KeyEvent.KEYCODE_F6),
        F7(keyValue = "F7", keyCode = KeyEvent.KEYCODE_F7),
        F8(keyValue = "F8", keyCode = KeyEvent.KEYCODE_F8),
        F9(keyValue = "F9", keyCode = KeyEvent.KEYCODE_F9),
        F10(keyValue = "F10", keyCode = KeyEvent.KEYCODE_F10),
        F11(keyValue = "F11", keyCode = KeyEvent.KEYCODE_F10),
        F12(keyValue = "F12", keyCode = KeyEvent.KEYCODE_F12),
    }

    enum class Option(
        override val keyValue: String,
        override val keyCode: Int
    ) : Key {
        Copy(keyValue = "Copy", keyCode = KeyEvent.KEYCODE_COPY),
        Paste(keyValue = "Paste", keyCode = KeyEvent.KEYCODE_PASTE),
        SelectAll(keyValue = "SelectAll", keyCode = 0),
        Cut(keyValue = "Cut", keyCode = KeyEvent.KEYCODE_CUT),
        Undo(keyValue = "Undo", keyCode = 0),
        Redo(keyValue = "Redo", keyCode = 0),
        Find(keyValue = "Find", keyCode = 0),
        Replace(keyValue = "Replace", keyCode = 0),
        Print(keyValue = "Print", keyCode = KeyEvent.KEYCODE_SYSRQ),
        Save(keyValue = "Save", keyCode = 0),
        Open(keyValue = "Open", keyCode = 0),
        Help(keyValue = "Help", keyCode = 0),
        About(keyValue = "About", keyCode = 0),
        Quit(keyValue = "Quit", keyCode = 0),
        FullScreen(keyValue = "FullScreen", keyCode = 0),
        Minimize(keyValue = "Minimize", keyCode = 0),
        Maximize(keyValue = "Maximize", keyCode = 0),
        Close(keyValue = "Close", keyCode = 0),
    }

}