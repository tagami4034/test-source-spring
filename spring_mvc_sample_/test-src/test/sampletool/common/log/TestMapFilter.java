package test.sampletool.common.log;

import java.util.Map;
import java.util.Properties;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

public class TestMapFilter {
	static private Logger log = LoggerFactory.getLogger(TestMapFilter.class);
	
	/**
	 * 文字列からMapに変換できるかのテスト
	 * @throws Exception
	 */
	@Test
	public void testConvert() throws Exception{
		String convertMap = "key,org.springframework.jdbc.datasource.DataSourceTransactionManager=Creating new transaction\n"
				+ "msg,org.springframework.jdbc.datasource.DataSourceTransactionManager=トランザクション開始";
		//
		FormattingConversionServiceFactoryBean factoryBean = new FormattingConversionServiceFactoryBean();
		factoryBean.afterPropertiesSet();
		ConversionService conversionService = factoryBean.getObject();
		Map<Object, Object> prop = conversionService.convert(convertMap, Properties.class);
		log.info(prop.toString());
	}
}
