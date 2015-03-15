package ca.syncron.network.tcp.node;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

/**
 * Created by Dawson on 3/14/2015.
 */
public class NodeClientBundler {
	static              String           nameId = NodeClientBundler.class.getSimpleName();
	public final static org.slf4j.Logger log    = LoggerFactory.getLogger(nameId);

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, MULTI_LINE_STYLE);
	}

	public volatile Map<String, NodeBundle> nodes = new HashMap<>();

	public NodeClientBundler() {}


	public void add(NodeBundle bundle) {
		nodes.put(bundle.userId, bundle);
	}

	public NodeBundle get(String nodeId) {
		return nodes.get(nodeId);
	}

	public NodeBundle newBundle() {
		return new NodeBundle();
	}

	//
///////////////////////////////////////////////////////
	public class NodeBundle {
		NodeBundle() {
		}

		NodeBundle(String serverId, String userId) {
			this.serverId = serverId;
			this.userId = userId;
		}

		public          String serverId    = "";
		public          String userId      = "";
		public volatile int[]  analogVals  = new int[12];
		public volatile int[]  digitalVals = new int[12];

		@Override
		public String toString() {
			return ReflectionToStringBuilder.toString(this, MULTI_LINE_STYLE);
		}
	}
}
