package pers.aprakash.spanqit.examples;

import org.junit.Test;

import pers.aprakash.spanqit.constraint.Expression;
import pers.aprakash.spanqit.constraint.Expressions;
import pers.aprakash.spanqit.core.Prefix;
import pers.aprakash.spanqit.core.QueryPattern;
import pers.aprakash.spanqit.core.Spanqit;
import pers.aprakash.spanqit.core.Variable;
import pers.aprakash.spanqit.graphpattern.GraphPattern;
import pers.aprakash.spanqit.graphpattern.GraphPatternNotTriple;
import pers.aprakash.spanqit.graphpattern.GraphPatterns;
import pers.aprakash.spanqit.rdf.IRI;

public class Section8 extends BaseExamples {
	@Test
	public void example_8_1_1() {
		String rdf_ns = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
		Prefix rdf = Spanqit.prefix("rdf", iri(rdf_ns));
		Prefix foaf = Spanqit.prefix("foaf", iri(FOAF_NS));
		Variable person = query.var();

		GraphPattern personWithName = person.has(foaf.iri("name"),
				Spanqit.var("name"));
		GraphPatternNotTriple personOfTypePerson = GraphPatterns.and(person
				.has(rdf.iri("type"), foaf.iri("Person")));
		query.prefix(rdf, foaf).select(person)
				.where(personOfTypePerson.filterNotExists(personWithName));
		p();
	}

	@Test
	public void example_8_1_2() {
		String rdf_ns = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
		Prefix rdf = Spanqit.prefix("rdf", iri(rdf_ns));
		Prefix foaf = Spanqit.prefix("foaf", iri(FOAF_NS));
		Variable person = query.var();

		GraphPattern personWithName = person.has(foaf.iri("name"),
				Spanqit.var("name"));
		GraphPatternNotTriple personOfTypePerson = GraphPatterns.and(person
				.has(rdf.iri("type"), foaf.iri("Person")));
		query.prefix(rdf, foaf).select(person)
				.where(personOfTypePerson.filterExists(personWithName));
		p();
	}

	@Test
	public void example_8_2() {
		Prefix base = Spanqit.prefix(iri("http://example/"));
		Prefix foaf = Spanqit.prefix("foaf", iri(FOAF_NS));
		Variable s = query.var();

		GraphPattern allNotNamedBob = GraphPatterns.and(
				s.has(query.var(), query.var())).minus(
				s.has(foaf.iri("givenName"), literal("Bob")));
		query.prefix(base, foaf).select(s).distinct().where(allNotNamedBob);
		p();
	}

	@Test
	public void example_8_3_2() {
		Prefix base = Spanqit.prefix(iri("http://example/"));
		Variable s = query.var(), p = query.var(), o = query.var();
		IRI a = base.iri("a"), b = base.iri("b"), c = base.iri("c");
		
		query.prefix(base).all().where(GraphPatterns.and(s.has(p, o)).filterNotExists(GraphPatterns.tp(a, b, c)));
		p();
		
		QueryPattern where = Spanqit.where(GraphPatterns.and(s.has(p, o)).minus(GraphPatterns.tp(a, b, c)));
		
		// passing a QueryPattern object to the query (rather than graph 
		// pattern(s)) replaces (rather than augments) the query's
		// query pattern. This allows reuse of the other elements of the query.
		query.where(where);
		p();
	}
	
	@Test
	public void example_8_3_3() {
		Prefix base = Spanqit.prefix(iri("http://example/"));
		Variable x = query.var(), m = query.var(), n = query.var();
		Expression<?> filter = Expressions.equals(n, m);
		
		GraphPattern notExistsFilter = GraphPatterns.and(x.has(base.iri("p"), n))
				.filterNotExists(GraphPatterns.and(x.has(base.iri("q"), m))
						.filter(filter));
		
		query.prefix(base).select().all().where(notExistsFilter);
		p();
		
		QueryPattern where = Spanqit.where(GraphPatterns.and(x.has(base.iri("p"), n))
				.minus(GraphPatterns.and(x.has(base.iri("q"), m)).filter(filter)));
		query.where(where);
		p();
	}
}