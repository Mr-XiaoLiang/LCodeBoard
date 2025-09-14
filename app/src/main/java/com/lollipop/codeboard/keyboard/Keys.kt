package com.lollipop.codeboard.keyboard

object Keys {

    interface Key {
        val keyValue: String
    }

    val keyList = arrayOf<List<Key>>(
        Letter.entries,
        Number.entries,
        Symbol.entries,
        Decoration.entries,
        Function.entries
    )

    fun findKey(keyValue: String): Key? {
        if (keyValue.isEmpty()) {
            return null
        }
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

    enum class Letter(
        override val keyValue: String
    ) : Key {
        a("a"),
        b("b"),
        c("c"),
        d("d"),
        e("e"),
        f("f"),
        g("g"),
        h("h"),
        i("i"),
        j("j"),
        k("k"),
        l("l"),
        m("m"),
        n("n"),
        o("o"),
        p("p"),
        q("q"),
        r("r"),
        s("s"),
        t("t"),
        u("u"),
        v("v"),
        w("w"),
        x("x"),
        y("y"),
        z("z"),
        A("A"),
        B("B"),
        C("C"),
        D("D"),
        E("E"),
        F("F"),
        G("G"),
        H("H"),
        I("I"),
        J("J"),
        K("K"),
        L("L"),
        M("M"),
        N("N"),
        O("O"),
        P("P"),
        Q("Q"),
        R("R"),
        S("S"),
        T("T"),
        U("U"),
        V("V"),
        W("W"),
        X("X"),
        Y("Y"),
        Z("Z"),
    }

    enum class Number(
        override val keyValue: String
    ) : Key {
        Num1("1"),
        Num2("2"),
        Num3("3"),
        Num4("4"),
        Num5("5"),
        Num6("6"),
        Num7("7"),
        Num8("8"),
        Num9("9"),
        Num0("0"),
    }

    enum class Symbol(
        override val keyValue: String
    ) : Key {
        Exclamation("!"),
        At("@"),
        Hash("#"),
        Dollar("$"),
        Percent("%"),
        Caret("^"),
        Ampersand("&"),
        Asterisk("*"),
        ParenthesesLeft("("),
        ParenthesesRight(")"),
        Underscore("_"),
        Plus("+"),
        CurlyLeft("{"),
        CurlyRight("}"),
        VerticalBar("|"),
        Colon(":"),
        QuoteSingle("'"),
        QuoteDouble("\""),
        Less("<"),
        Greater(">"),
        Question("?"),
        SlashForward("/"),
        SlashBack("\\"),
        Tilde("~"),
        QuoteBack("`"),
        Comma(","),
        Dot("."),
        Semicolon(";"),
        Hyphen("-"),
        Equals("="),
        SquareLeft("["),
        SquareRight("]"),
    }

    enum class Decoration(
        override val keyValue: String
    ) : Key {
        Shift("Shift"),
        Command("Command"),
        Option("Option"),
        Backspace("Backspace"),
        Enter("Enter"),
        Space("Space"),
        Delete("Delete"),
        Tab("Tab"),
        Escape("Escape"),
        CapsLock("CapsLock"),
        ArrowUp("ArrowUp"),
        ArrowDown("ArrowDown"),
        ArrowLeft("ArrowLeft"),
        ArrowRight("ArrowRight"),
        Symbol("Symbol"),
        Language("Language")
    }

    enum class Function(
        override val keyValue: String
    ) : Key {
        F1("F1"),
        F2("F2"),
        F3("F3"),
        F4("F4"),
        F5("F5"),
        F6("F6"),
        F7("F7"),
        F8("F8"),
        F9("F9"),
        F10("F10"),
        F11("F11"),
        F12("F12"),
        Fn("Fn"),
    }

}