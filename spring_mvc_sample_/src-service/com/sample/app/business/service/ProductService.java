package com.sample.app.business.service;

import java.util.List;

import org.springframework.dao.OptimisticLockingFailureException;

import com.sample.app.business.model.Product;
import com.sample.app.business.model.ProductSearchKeys;

public interface ProductService {

	enum InsertResult{
		/** 成功 */
		OK,
		/** 重複以外のエラー */
		ERR_SYSTEM,
		/** アカウントの重複エラー */
		ERR_ACCOUT_DUPLICATED
	};

	/**
	 * 商品マスタを取得する
	 * @param procd
	 * @return
	 */
	public Product obtainProducts(String procd);

	/**
	 * 商品マスタを取得する
	 * @param procd
	 * @return
	 */
	public Product obtainZaikoProducts(String procd);

	/**
	 * 検索キーでメンバーのリストを取得する
	 * @param searchKeys
	 * @return
	 */
	public List<Product> findProducts(ProductSearchKeys searchKeys);

	/**
	 * 検索キーでマッチしたメンバーの数を取得する
	 * @param searchKeys
	 * @return
	 */
	public int countProducts(ProductSearchKeys searchKeys);

	/**
	 * メンバー情報を更新する。
	 * @param user
	 * @throws OptimisticLockingFailureException 他のユーザが更新しているとき
	 */
	public void updateProducts(Product pro)
			throws OptimisticLockingFailureException;

	public void updateZaikoProducts(Product pro)
			throws OptimisticLockingFailureException;

	/**
	 * メンバー登録。例外は返さないこと。
	 * 失敗時は、id=-1　を設定して返すこと。
	 * @return 登録結果。
	 */
	public InsertResult insertProducts(Product pro);
}
