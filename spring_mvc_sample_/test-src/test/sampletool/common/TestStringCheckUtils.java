package test.sampletool.common;

import static org.junit.Assert.*;

import org.junit.Test;

import com.sampletool.common.StringCheckUtils;

public class TestStringCheckUtils {

	@Test
	public void test_isHankaku() throws Exception{
		//ブランクのテスト
		assertTrue(StringCheckUtils.isHankaku(null));
		assertTrue(StringCheckUtils.isHankaku(""));
		//
		assertTrue(StringCheckUtils.isHankaku("zxcvbnnm,./\\]:;lkjhgffdsaqwertyyuiop@[^-0987665434321"));
		assertTrue(StringCheckUtils.isHankaku(" "));
		//全角
		assertFalse(StringCheckUtils.isHankaku(" あ"));
		assertFalse(StringCheckUtils.isHankaku("漏"));
		assertFalse(StringCheckUtils.isHankaku("１２３４５６７８９０"));
		assertFalse(StringCheckUtils.isHankaku("[.]*あ"));
		//特殊
		assertTrue(StringCheckUtils.isHankaku("\n\t\b"));
		assertTrue(StringCheckUtils.isHankaku("\r\n"));
		assertFalse(StringCheckUtils.isHankaku("\r\nあ"));
		assertFalse(StringCheckUtils.isHankaku("\r\n  薙 "));
		assertFalse(StringCheckUtils.isHankaku("\r\n ―"));
	}
	
	@Test
	public void test_isHiragana()throws Exception{
		//ブランクのテスト
		assertTrue(StringCheckUtils.isHiragana(null));
		assertTrue(StringCheckUtils.isHiragana(""));
		//
		assertTrue(StringCheckUtils.isHiragana("あいうえおかきくけこさしすせそたちつてとなにぬねのはひふへほまみむめもやゆよらりるれろわをん"));
		assertTrue(StringCheckUtils.isHiragana("がぎぐげござじずぜぞだじずぜぞばびぶべぼ"));
		assertTrue(StringCheckUtils.isHiragana("ぁぃぅぇぉゃゅょ"));
		assertTrue(StringCheckUtils.isHiragana("ゑ"));
		assertTrue(StringCheckUtils.isHiragana("ー"));
		//
		assertFalse(StringCheckUtils.isHiragana("あいうえおa"));
		assertFalse(StringCheckUtils.isHiragana("\r"));
		assertFalse(StringCheckUtils.isHiragana("\n"));
		assertFalse(StringCheckUtils.isHiragana("\t"));
		assertFalse(StringCheckUtils.isHiragana("\b"));
		//全角スペース
		assertFalse(StringCheckUtils.isHiragana("　"));
		//半角スペース
		assertFalse(StringCheckUtils.isHiragana(" "));
		assertFalse(StringCheckUtils.isHiragana("―"));
	}
	

	@Test
	public void test_isHiraganaNoHyphen()throws Exception{
		//ブランクのテスト
		assertTrue(StringCheckUtils.isHiraganaNoHyphen(null));
		assertTrue(StringCheckUtils.isHiraganaNoHyphen(""));
		//
		assertTrue(StringCheckUtils.isHiraganaNoHyphen("あいうえお"));
		assertFalse(StringCheckUtils.isHiraganaNoHyphen("ー"));
	}
	

	@Test
	public void test_isKatakana()throws Exception{
		//ブランクのテスト
		assertTrue(StringCheckUtils.isKatakana(null));
		assertTrue(StringCheckUtils.isKatakana(""));
		//
		assertTrue(StringCheckUtils.isKatakana("アイウエオカキクケコサシスセソタチツテトナニヌネノハヒフヘホマミムメモヤユヨラリルレロワヲン"));
		assertTrue(StringCheckUtils.isKatakana("ガギグゲゴザジズゼゾダジズゼゾバビブベボ"));
		assertTrue(StringCheckUtils.isKatakana("ァィゥェォャュョ"));
		assertTrue(StringCheckUtils.isKatakana("ヱ"));
		assertTrue(StringCheckUtils.isKatakana("ー"));
		//
		assertFalse(StringCheckUtils.isKatakana("アイウエオあ"));
		assertFalse(StringCheckUtils.isKatakana("\r"));
		assertFalse(StringCheckUtils.isKatakana("\n"));
		assertFalse(StringCheckUtils.isKatakana("\t"));
		assertFalse(StringCheckUtils.isKatakana("\b"));
		//全角スペース
		assertFalse(StringCheckUtils.isKatakana("　"));
		//半角スペース
		assertFalse(StringCheckUtils.isKatakana(" "));
		assertFalse(StringCheckUtils.isKatakana("―"));
	}
	

	@Test
	public void test_isZenkaku()throws Exception{
		//ブランクのテスト
		assertTrue(StringCheckUtils.isZenkaku(null));
		assertTrue(StringCheckUtils.isZenkaku(""));
		//
		assertTrue(StringCheckUtils.isZenkaku("あ漏薙＃‘＠＊＾"));
		assertFalse(StringCheckUtils.isZenkaku("あa"));
		assertFalse(StringCheckUtils.isZenkaku("\\"));
		assertFalse(StringCheckUtils.isZenkaku("\r"));
		assertFalse(StringCheckUtils.isZenkaku("\n"));
		assertFalse(StringCheckUtils.isZenkaku("\t"));
		assertFalse(StringCheckUtils.isZenkaku("\b"));
		assertFalse(StringCheckUtils.isZenkaku("\r\n"));
	}
	

	@Test
	public void test_isZenkakuMultiline() throws Exception{
		//ブランクのテスト
		assertTrue(StringCheckUtils.isZenkakuMultiline(null));
		assertTrue(StringCheckUtils.isZenkakuMultiline(""));
		//
		assertTrue(StringCheckUtils.isZenkakuMultiline("あ漏薙＃‘＠＊＾"));
		assertTrue(StringCheckUtils.isZenkakuMultiline("\r"));
		assertTrue(StringCheckUtils.isZenkakuMultiline("\n"));
		assertTrue(StringCheckUtils.isZenkakuMultiline("\r\n"));
		//
		assertFalse(StringCheckUtils.isZenkakuMultiline("あa"));
		assertFalse(StringCheckUtils.isZenkakuMultiline("\\"));
		assertFalse(StringCheckUtils.isZenkakuMultiline("\t"));
		assertFalse(StringCheckUtils.isZenkakuMultiline("\b"));
	}
	

	@Test
	public void test_isZenkakuNumber() throws Exception{
		//ブランクのテスト
		assertTrue(StringCheckUtils.isZenkakuNumber(null));
		assertTrue(StringCheckUtils.isZenkakuNumber(""));
		//
		assertTrue(StringCheckUtils.isZenkakuNumber("０１２３４５６７８９"));
		assertFalse(StringCheckUtils.isZenkakuNumber("\r"));
		assertFalse(StringCheckUtils.isZenkakuNumber("\n"));
		assertFalse(StringCheckUtils.isZenkakuNumber("\r\n"));
		//
		assertFalse(StringCheckUtils.isZenkakuNumber("あa"));
		assertFalse(StringCheckUtils.isZenkakuNumber("\\"));
		assertFalse(StringCheckUtils.isZenkakuNumber("０１あ"));
		assertFalse(StringCheckUtils.isZenkakuNumber("０ "));
	}
}
