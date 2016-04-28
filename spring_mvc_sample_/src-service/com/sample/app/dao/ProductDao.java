package com.sample.app.dao;

import java.util.List;

import org.springframework.dao.OptimisticLockingFailureException;

import com.sample.app.business.model.Product;
import com.sample.app.business.model.ProductSearchKeys;

public interface ProductDao {
	/**
	 * IDから商品オブジェクトを取得する。
	 * @param id
	 * @return 取得した商品。見つからない場合はNULL。
	 */
	public Product obtainProducts(String procd);
	public Product obtainProducts(int procd);

	public Product obtainZaikoProducts(String procd);
	/**
	 * 検索キーにマッチした商品を取得する
	 * @param searchKeys
	 * @return マッチした商品。1件もない場合は0件のListを返す。
	 */
	public List<Product> findProducts(ProductSearchKeys searchKeys);

	/**
	 * 検索キーにマッチした商品の数を取得する。
	 * @param searchKeys
	 * @return
	 */
	public int countProducts(ProductSearchKeys searchKeys);

	/**<pre>
	 * 商品を更新する。バージョンが楽観的ロックのキーになっており、IDとバージョンが一致するレコードが見つからない場合は
	 * 楽観的ロック例外を投げる。
	 * </pre>
	 * @param user [in]更新する商品
	 * <br>　　　[out]更新日に現在日時、インクリメントされたバージョンが設定されたオブジェクト
	 * @throws OptimisticLockingFailureException 楽観的ロックエラー
	 */
	public void updateProduct(Product user) throws OptimisticLockingFailureException;

	public void updateZaikoProduct(Product product) throws OptimisticLockingFailureException;

	/**新規商品の登録をする。
	 * 失敗した場合は例外を投げる。このとき、ID、更新日は不正になっている可能性がある。
	 * @param user [in]挿入する商品
	 * <br>　　　[out]ID,更新日が設定されたオブジェクト。
	 */
	public void insertProduct(Product user);
}
