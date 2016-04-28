package test.sampletool.common;

import java.io.Writer;

import org.junit.Test;

import com.thoughtworks.xstream.XStream;

import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;

public class TestPaging {

	@Test
	public void test() throws Exception{
		/*XStream xstream = new XStream(new JettisonMappedXmlDriver(){ 
			public HierarchicalStreamWriter createWriter(Writer writer) {
				char[] ret = new char[]{' '};
				return new JsonWriter(writer, JsonWriter.STRICT_MODE, new JsonWriter.Format(ret, ret, JsonWriter.Format.COMPACT_EMPTY_ELEMENT ));
	    }} );
        xstream.setMode(XStream.NO_REFERENCES);
        xstream.omitField(Po.class, "po");
        xstream.aliasType(Po.class.getSimpleName(), Po.class);
        //xstream.ignoreUnknownElements("po");
        //xstream.denyTypes(new Class[]{Po.class});
        //
        Po p = new Po();
        Po p2 = new Po();
        p.setA("aaa");
        p.setI(120);
        p2.setI(1111);
        p.setPo(p);
        System.out.println(xstream.toXML(p));
        
        String json = "{\"com.soracane.batchviewer.business.domain.Test1$Po\":{\"a\":\"aaa\",\"i\":120,\"po\":{\"i\":1111}}}"; //xstream.toXML(p);
        p2 = (Po)xstream.fromXML(json, new Po());
        System.out.println("end");
        */
	}
	
	static public class Po{
		private String a;
		private int i;
		private Po po;
		public String getA() {
			return a;
		}
		public void setA(String a) {
			this.a = a;
		}
		public int getI() {
			return i;
		}
		public void setI(int i) {
			this.i = i;
		}
		public Po getPo() {
			return po;
		}
		public void setPo(Po po) {
			this.po = po;
		}
		
	}
}
