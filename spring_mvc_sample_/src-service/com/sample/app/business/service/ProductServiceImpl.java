package com.sample.app.business.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.sample.app.business.model.Product;
import com.sample.app.business.model.ProductSearchKeys;
import com.sample.app.dao.ProductDao;

@Service
public class ProductServiceImpl implements ProductService {
	private ProductDao ProductDao;

	public ProductDao getProductDao() {
		return ProductDao;
	}

	@Autowired
	public void setProductDao(ProductDao ProductDao) {
		this.ProductDao = ProductDao;
	}

	@Override
	public Product obtainProducts(String procd){
		return this.ProductDao.obtainProducts(procd);
	}



	@Override
	public void updateProducts(Product pro) throws OptimisticLockingFailureException {
		this.ProductDao.updateProduct(pro);
	}

	@Override
	public List<Product> findProducts(ProductSearchKeys searchKeys) {
		return this.ProductDao.findProducts(searchKeys);
	}

	@Override
	public int countProducts(ProductSearchKeys searchKeys) {
		return this.ProductDao.countProducts(searchKeys);
	}

	@Override
	public InsertResult insertProducts(Product pro) {
		try{
			pro.setProcd("");
			this.ProductDao.insertProduct(pro);
			return InsertResult.OK;
		}catch(DuplicateKeyException e){
			return InsertResult.ERR_ACCOUT_DUPLICATED;
		}catch(Exception e){
		}
		return InsertResult.ERR_SYSTEM;
	}

	@Override
	public void updateZaikoProducts(Product pro)
			throws OptimisticLockingFailureException {
		// TODO 自動生成されたメソッド・スタブ
		this.ProductDao.updateZaikoProduct(pro);
	}

	@Override
	public Product obtainZaikoProducts(String procd) {
		// TODO 自動生成されたメソッド・スタブ
		return this.ProductDao.obtainZaikoProducts(procd);
	}
}
