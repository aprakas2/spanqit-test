package pers.aprakash.spanqit.examples;

import org.junit.Test;

import pers.aprakash.spanqit.constraint.Expression;
import pers.aprakash.spanqit.constraint.ExpressionOperands;
import pers.aprakash.spanqit.constraint.Expressions;
import pers.aprakash.spanqit.core.Assignment;
import pers.aprakash.spanqit.core.Prefix;
import pers.aprakash.spanqit.core.Spanqit;
import pers.aprakash.spanqit.core.Variable;

public class Section11 extends BaseExamples {
	@Test
	public void example_11_1() {
		Prefix base = Spanqit.prefix(iri("http://books.example/"));
		Variable lprice = Spanqit.var("lprice"), totalPrice = Spanqit.var("totalPrice");
		
		Expression<?> sum = Expressions.sum(lprice);
		Assignment sumAsTotal = Spanqit.as(sum, totalPrice);
		
		Variable org = Spanqit.var("org"), auth = Spanqit.var("auth"),
				book = Spanqit.var("book");
		
		query.prefix(base).select(sumAsTotal).where(org.has(base.iri("affiliates"), auth),
				auth.has(base.iri("writesBook"), book), book.has(base.iri("price"), lprice))
				.groupBy(org).having(Expressions.gt(sum, 10));
		p();
	}
	
	@Test
	public void example_11_2() {
		Prefix base = Spanqit.prefix(null);
		Variable y = query.var(), avg = query.var(), a = query.var(), x = query.var();
		
		query.select(Spanqit.as(Expressions.avg(y), avg)).where(a.has(base.iri("x"), x), a.has(base.iri("y"), y)).groupBy(x);
		p();
	}
	
	@Test
	public void example_11_3() {
		Prefix base = Spanqit.prefix(iri("http://data.example/"));
		Variable size = Spanqit.var("size"), asize = Spanqit.var("asize"), x = query.var();
		Expression<?> avgSize = Expressions.avg(size);
		
		query.prefix(base)
			.select(avgSize.as(asize))
			.where(x.has(base.iri("size"), size))
			.groupBy(x)
			.having(Expressions.gt(avgSize, 10));
		p();
	}
	
	@Test
	public void example_11_4() {
		Prefix base = Spanqit.prefix(iri("http://example.com/data/#"));
		Variable x = query.var(), y = query.var(), z = query.var(), min = query.var();
		Expression<?> twiceMin = Expressions.multiply(Expressions.min(y), ExpressionOperands.numberOperand(2));
		
		query.prefix(base)
			.select(x, twiceMin.as(min))
			.where(x.has(base.iri("p"), y), x.has(base.iri("q"), z))
			.groupBy(x, Expressions.str(z));
		p();
	}
	
	@Test
	public void example_11_5() {
		Prefix base = Spanqit.prefix(iri("http://example.com/data/#"));
		Variable g = query.var(), p = query.var(), avg = query.var(), c = query.var();
		// TODO: fix paranthecising (sp?) compound expressions
		Expression<?> midRange = Expressions.divide(Expressions.add(Expressions.min(p), Expressions.max(p)), ExpressionOperands.numberOperand(2));
		
		query.prefix(base)
			.select(g, Expressions.avg(p).as(avg), midRange.as(c))
			.where(g.has(base.iri("p"), p))
			.groupBy(g);
		p(); // almost...
	}
}