package com.sample.app.dao.mybatis;

import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;
import org.joda.time.DateTime;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Repository;

import com.sample.app.business.model.Product;
import com.sample.app.business.model.ProductSearchKeys;
import com.sample.app.dao.ProductDao;

@Repository
public class ProductDaoImpl extends SqlSessionDaoSupport implements ProductDao {
	@Autowired
	public ProductDaoImpl(SqlSessionFactory sqlSessionFactory) {
		super.setSqlSessionFactory(sqlSessionFactory);
	}

	@Override
	public Product obtainProducts(String procd) {
		Product pro = getSqlSession().selectOne("mybatis.sample.Product.obtain", procd);
		return pro;
	}

	@Override
	public Product obtainProducts(int procd) {
		Product pro = getSqlSession().selectOne("mybatis.sample.Product.obtain", procd);
		return pro;
	}

	@Override
	public List<Product> findProducts(ProductSearchKeys searchKeys) {
		return getSqlSession().selectList("mybatis.sample.Product.prozaikofind", searchKeys);
	}

	@Override
	public int countProducts(ProductSearchKeys searchKeys) {
		return getSqlSession().selectOne("mybatis.sample.Product.count", searchKeys);
	}

	@Override
	public void updateProduct(Product pro) throws OptimisticLockingFailureException {
		pro.setUpDate(new DateTime());
		int num = getSqlSession().update("mybatis.sample.Product.update", pro);
		//楽観的ロック失敗時
		if(num != 1) throw new ObjectOptimisticLockingFailureException(pro.getClass(), pro);

	}

	@Override
	public void insertProduct(Product pro) {
		pro.setUpDate(new DateTime());
		getSqlSession().insert("mybatis.sample.Product.insert", pro);
	}

	@Override
	public void updateZaikoProduct(Product product)
			throws OptimisticLockingFailureException {
		// TODO 自動生成されたメソッド・スタブ
		product.setUpDate(new DateTime());
		int num = getSqlSession().update("mybatis.sample.Product.zaikoUpdate", product);
		//楽観的ロック失敗時
		if(num != 1) throw new ObjectOptimisticLockingFailureException(product.getClass(), product);
	}

	@Override
	public Product obtainZaikoProducts(String procd) {
		// TODO 自動生成されたメソッド・スタブ
		Product pro = getSqlSession().selectOne("mybatis.sample.Product.obtainZaiko", procd);
		return pro;
	}
}
