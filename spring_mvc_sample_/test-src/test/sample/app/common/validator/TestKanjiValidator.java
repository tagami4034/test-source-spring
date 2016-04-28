package test.sample.app.common.validator;

import static org.junit.Assert.*;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Test;

import com.sampletool.common.validator.annotation.Kanji;
import com.sampletool.common.validator.annotation.Kanji.CheckType;

public class TestKanjiValidator {
	@Kanji(type=CheckType.ZENKAKU)
	protected String zenkaku;
	@Kanji(type=CheckType.ZENKAKU_LINES)
	protected String zenkakuLines;
	@Kanji(type=CheckType.HANKAKU)
	protected String hankaku;
	@Kanji(type=CheckType.HIRAGANA)
	protected String hiragana;
	@Kanji(type=CheckType.KATAKANA)
	protected String katakana;
	@Kanji(type=CheckType.NUMBER)
	protected String number;
	
	
	@Test
	public void testZenkaku() throws Exception{
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = validatorFactory.getValidator();
		
		//正しい全角
		TestKanjiValidator bean = new TestKanjiValidator();
		bean.zenkaku = "あいうえお";
		assertEquals(validator.validate(bean).size(), 0);
		
		//英字が混じっている
		bean.zenkaku = "あいaうえお";
		assertEquals(validator.validate(bean).size(), 1);
	}

	@Test
	public void testZenkakuLines() throws Exception{
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = validatorFactory.getValidator();
		
		//正しい全角
		TestKanjiValidator bean = new TestKanjiValidator();
		bean.zenkakuLines = "あいう\nえ\nお";
		assertEquals(validator.validate(bean).size(), 0);
		
		//正しい全角改行なし
		bean = new TestKanjiValidator();
		bean.zenkakuLines = "あいうえお";
		assertEquals(validator.validate(bean).size(), 0);
		
		//英字が混じっている
		bean.zenkaku = "あい\naうえお";
		assertEquals(validator.validate(bean).size(), 1);
	}

	@Test
	public void testHankaku() throws Exception{
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = validatorFactory.getValidator();
		
		//正しい半角
		TestKanjiValidator bean = new TestKanjiValidator();
		bean.hankaku = "zxcvbnm,./\\]:;lkjhgfdsaqwertyuiop@[^-0987654321";
		assertEquals(validator.validate(bean).size(), 0);
		
		//英字が混じっている
		bean.hankaku = "あいaうえお";
		assertEquals(validator.validate(bean).size(), 1);
	}
	
	

	@Test
	public void testHiragana() throws Exception{
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = validatorFactory.getValidator();
	
		//正しいひらがな
		TestKanjiValidator bean = new TestKanjiValidator();
		bean.hiragana = "あいうえお";
		assertEquals(validator.validate(bean).size(), 0);
		
		//カタカナが混じっている
		bean.hiragana = "アあいうお";
		assertEquals(validator.validate(bean).size(), 1);
		
		//改行が混じっている
		bean.hiragana = "あいうえ\nかき";
		assertEquals(validator.validate(bean).size(), 1);
	}

	@Test
	public void testKatakana() throws Exception{
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = validatorFactory.getValidator();
	
		//正しいひらがな
		TestKanjiValidator bean = new TestKanjiValidator();
		bean.katakana = "アイウエオ";
		assertEquals(validator.validate(bean).size(), 0);
		
		//ひらがなが混じっている
		bean.katakana = "あアイウエオ";
		assertEquals(validator.validate(bean).size(), 1);
		
		//改行が混じっている
		bean.katakana = "アイウエオ\nカキク";
		assertEquals(validator.validate(bean).size(), 1);
	}

	@Test
	public void testNumber() throws Exception{
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = validatorFactory.getValidator();
	
		//正しいひらがな
		TestKanjiValidator bean = new TestKanjiValidator();
		bean.number = "１２３４５";
		assertEquals(validator.validate(bean).size(), 0);
		
		//ひらがなが混じっている
		bean.number = "１２３あ１２３";
		assertEquals(validator.validate(bean).size(), 1);
		
		//改行が混じっている
		bean.number = "１２３\n３４５";
		assertEquals(validator.validate(bean).size(), 1);
	}
}
