package net.lushmc.core.utils.placeholders;

import java.util.ArrayList;
import java.util.List;

public enum Emoticons {

	SWORD("\uD83D\uDDE1", EmoticonType.TOOL), BOW("\uD83C\uDFF9", EmoticonType.TOOL),
	TRIDENT("\uD83D\uDD31", EmoticonType.TOOL), POTION("\uD83E\uDDEA", EmoticonType.TOOL),
	SPLASH_POTION("\u2697", EmoticonType.TOOL), FISHING_ROD("\uD83C\uDFA3", EmoticonType.TOOL),
	SHIELD("\uD83D\uDEE1", EmoticonType.TOOL), AXE("\uD83E\uDE93", EmoticonType.TOOL),
	STAR_1("\u2605", EmoticonType.STAR), STAR_2("\u2606", EmoticonType.STAR), STAR_3("\u272A", EmoticonType.STAR),
	STAR_4("\u272A", EmoticonType.STAR), STAR_5("\u272F", EmoticonType.STAR), STAR_6("\u066D", EmoticonType.STAR),
	STAR_7("\u272D", EmoticonType.STAR), STAR_8("\u2730", EmoticonType.STAR), STAR_9("\u269D", EmoticonType.STAR),
	STAR_10("\u2734", EmoticonType.STAR), STAR_11("\u2733", EmoticonType.STAR), STAR_12("\u272B", EmoticonType.STAR),
	STAR_13("\u235F", EmoticonType.STAR), STAR_14("\u2727", EmoticonType.STAR), STAR_15("\u2742", EmoticonType.STAR),
	SUN("\u2600", EmoticonType.STAR), NITRO("\u25C6", EmoticonType.STAR), WARNING("\u26A0", EmoticonType.TOOL),
	MULTIPLICATION("\u2716"), CHECK_MARK("\u2714"), MUSIC_NOTE_1("\u266A", EmoticonType.MUSIC),
	MUSIC_NOTE_2("\u2669", EmoticonType.MUSIC), MUSIC_NOTE_3("\u266B", EmoticonType.MUSIC),
	MUSIC_NOTE_4("\u266C", EmoticonType.MUSIC), SCISSORS_1("\u2704"), SCISSORS_2("\u2702"), ENVELOPE("\u2709"),
	COMET("\u2604"), SPARKLE_SMALL("\u0FCF"), SPARLE_LARGE("\u2042"), SPARKLE_CIRCLE("\uA670"), HEART_1("\u2764"),
	N0("\u24EA", EmoticonType.NUMBER), N1("\u2460", EmoticonType.NUMBER), N2("\u2461", EmoticonType.NUMBER),
	N3("\u2462", EmoticonType.NUMBER), N4("\u2463", EmoticonType.NUMBER), N5("\u2464", EmoticonType.NUMBER),
	N6("\u2465", EmoticonType.NUMBER), N7("\u2466", EmoticonType.NUMBER), N8("\u2467", EmoticonType.NUMBER),
	N9("\u2468", EmoticonType.NUMBER), N10("\u2469", EmoticonType.NUMBER), N11("\u246A", EmoticonType.NUMBER),
	N12("\u246B", EmoticonType.NUMBER), N13("\u246C", EmoticonType.NUMBER), N14("\u246D", EmoticonType.NUMBER),
	N15("\u246E", EmoticonType.NUMBER), N16("\u246F", EmoticonType.NUMBER), N17("\u2470", EmoticonType.NUMBER),
	N18("\u2471", EmoticonType.NUMBER), N19("\u2472", EmoticonType.NUMBER), N20("\u2473", EmoticonType.NUMBER),
	BAR_0("\u2588", EmoticonType.TEXT), BAR_1("\u258C", EmoticonType.TEXT), BAR_2("\u258F", EmoticonType.TEXT),
	GEMS("\u2743"), FLOWER1("\u273F"), FLOWER2("\u2740"), FLOWER3("\uD83E\uDD40"), SHAMROCK("\u2618"),
	RIGHT_ARROW("\u00BB", EmoticonType.ARROW), UNKNOWN("???");

	String unicode;
	EmoticonType[] types;

	Emoticons(String unicode) {
		this(unicode, EmoticonType.DEFAULT);
	}

	Emoticons(String unicode, EmoticonType... types) {
		this.unicode = unicode;
		this.types = types;
	}

	@Override
	public String toString() {
		return unicode;
	}

	public List<EmoticonType> getTypes() {
		List<EmoticonType> r = new ArrayList<>();
		for (EmoticonType t : types)
			r.add(t);
		return r;
	}

}
