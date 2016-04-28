package com.sample.app.controller;

import java.util.List;

import javax.validation.Valid;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sample.app.business.model.Product;
import com.sample.app.business.model.ProductSearchKeys;
import com.sample.app.business.service.MemberService;
import com.sample.app.business.service.ProductService;
import com.sample.app.common.ErrorUtil;
import com.sampletool.common.joda.JodaDateTimeEditor;


@Controller
@RequestMapping(value="/zaiko/product")
public class ProductController extends ProductControllerBase {

	@Autowired
	protected ProductService productService;
	protected MemberService memberService;

	//コンストラクタ===========================================-
	public ProductController() {
		super("zaiko/");
	}

	/*リクエスト初期化処理===========================================
	/**
	 * formモデルのバインダーの初期化。リクエストパラメタをモデルに変換するたびに呼ばれる。
	 */
	@InitBinder("form")
	public void initBinderForm(WebDataBinder binder) {
		//バインドするときの日付のフォーマット指定。
		binder.registerCustomEditor(DateTime.class, "product.upDate",
			new JodaDateTimeEditor("yyyy/MM/dd HH:mm:ss.SSS"));

		//Memberオブジェクトのうち、member.nameパラメタを受け取りたくない場合。攻撃による期待しない値の変更を防ぐ。
		binder.setAllowedFields("product.tanka","product.name", "product.role",
			"productSearchKeys.procd","productSearchKeys.nameBW","productSearchKeys.tankaTo",
			"productSearchKeys.tankaFrom","paging.searchKeys", "paging.pageSize", "paging.specifiedPageNum",
			"strSearchKeys","productSearchKeys.tanka","productSearchKeys.proClass","product.kazu",
			"product.proClass");
	}


	/**
	 * モデルオブジェクトの初期化
	 * MemberオブジェクトをDBから取得する。
	 * 入力画面、確認画面、完了画面それぞれで使用される。
	 * setAllowedFieldsでパラメタの値が設定されないプロパティはここで取得したDBの値が設定されている。
	 * そのため、後でDaoがPOJOの値ですべてのカラムを更新しても、nullになったりしない。
	 */
	@ModelAttribute("form")
	public Form newRequest(
			@RequestParam(required=false, value="product.procd") String procd
	) {

		Form f = new Form();
		f.setAdmin(true);
		if(procd != null){
			//ユーザ情報取得
			Product pro = this.productService.obtainProducts(procd);
			f.setProduct(pro);
		}


/*
		Form f = new Form();
		f.setAdmin(true);
		if(procd != null){
			//ユーザ情報取得
			Member mem = this.memberService.obtainMember(1);
			f.setMember(mem);
		}*/

		return f;
	}

	/*リクエスト処理===========================================
	更新と参照などの処理ごとにControllerを作成しても良いと思う。
	あまりクラス内のコード量が増えると見えずらくなるので注意。
	*/

	///一覧検索の入力処理
	@RequestMapping(value="srch/input", method=RequestMethod.GET)
	public String findInput(Form form) {
		//既にnewRequestでモデルをDBから取り出し、設定しているので何もする必要がない
		form.getPaging().reset(1, 5);
		return "zaiko/product-Srch-Input";
	}

	///一覧検索の入力処理
	@RequestMapping(value="srch/input", method=RequestMethod.POST)
	public String findInputFromEdit(Form form) {
		return "zaiko/product-Srch-Input";
	}

	///一覧検索結果
	@RequestMapping(value="srch/list", method=RequestMethod.POST)
	public String findList(@Valid Form form, BindingResult result) {
		//@Validを指定したモデルは妥当性チェックが実行される。
		if(ErrorUtil.checkInvalidAndWriteLog(result)){
			return "zaiko/product-Srch-Input";
		}
		//サイズチェック
		if(form.getPaging().getPageSize() > 100) form.getPaging().setPageSize(100);
		form.getPaging().setPageListSize(5);

		//検索キー
		ProductSearchKeys searchKeys = form.getProductSearchKeys();
		searchKeys.setOrderBy(ProductSearchKeys.ORDER_PROCD);

		//検索結果数を取得する
		int cnt = this.productService.countProducts(searchKeys);
		if(ErrorUtil.checkListOverFlowCount(result, cnt, 1000)){
			return "zaiko/product-Srch-Input";
		}

		//検索をする
		List<Product> list = this.productService.findProducts(searchKeys);
		form.setProductList(list);
		form.getPaging().setCount(cnt);

		//監査ログ出力
		log.info(auditDaMarker, this.msg.auditFindData(searchKeys));

		return "zaiko/product-Srch-List";
	}



	//===========================================
	//フォーム(HTML用のパラメタを受け取れるように作っておいた方がよいと思います)
	/*public static class Form{
		static final String[] ROLE_LIST = new String[]{"ROLE_ADMIN", "ROLE_UPDATE", "ROLE_READ"};
		@Valid
		private ProductSearchKeys productSearchKeys;
		@Valid
		private WebPaging paging;
		private List<Product> productList;
		@Valid
		private Product product;
		private boolean isAdmin = false;

		public Form() {
			this.productSearchKeys = new ProductSearchKeys();
			this.productSearchKeys.setOrderBy(ProductSearchKeys.ORDER_PROCD);
			this.paging = new WebPaging(this.productSearchKeys, 0);
			this.paging.setPageListSize(5);
			this.paging.setPageSize(5);
			this.paging.putValid(100, 50);
		}

		public String[] getRoleList() {
			return ROLE_LIST;
		}

		public WebPaging getPaging() {
			return paging;
		}
		public void setPaging(WebPaging paging) {
			this.paging = paging;
		}

		public ProductSearchKeys getProductSearchKeys() {
			return productSearchKeys;
		}
		public void setProductSearchKeys(ProductSearchKeys productSearchKeys) {
			this.productSearchKeys = productSearchKeys;
		}

		public List<Product> getProductList() {
			return productList;
		}
		public void setProductList(List<Product> productList) {
			this.productList = productList;
		}

		public Product getProduct() {
			return product;
		}
		public void setProduct(Product product) {
			this.product = product;
		}

		public boolean isAdmin() {
			return isAdmin;
		}
		public void setAdmin(boolean isAdmin) {
			this.isAdmin = isAdmin;
		}
	}*/


}
