import att.grappa.*;

public class PrettyPrint {

	public static void main(String[] args) throws Exception {
		parser parser = new parser(System.in, System.err);
		parser.parse();
		DotGraph graph = parser.getGraph();
		graph.printGraph(System.out);
		System.exit(0);
	}
}
