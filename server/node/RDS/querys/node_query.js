const node_query = {
    addNode: (node) => {
        return `INSERT INTO "public"."rs_nodes" 
        ("osm_Id", "node_Id", "latitude", "longitude", "degree", "edges", "tags") 
        VALUES 
        (${node.osm_Id}, '${node.node_Id}', ${node.latitude}, ${node.longitude}, ${node.degree}, '${node.edges}', '${node.tags}');`;
    },
    updateNode: (node) => {
        return `UPDATE "public"."rs_nodes" SET 
                    "osm_Id" = ${node.osm_Id}, 
                    "latitude" = ${node.latitude}, 
                    "longitude" = ${node.longitude},
                    "degree" = ${node.degree}, 
                    "edges" = '${node.edges}', 
                    "tags" = '${node.tags}'
                WHERE "node_Id" = '${node.node_Id}';`;
    },
    deleteNodeById: (node) => {
        return `DELETE FROM "public"."rs_nodes" 
                WHERE "node_Id" = '${node.node_Id}';`;
    },
    getNodeById: (node) => {
        return `SELECT * FROM "public"."rs_nodes" 
        WHERE "node_Id" = '${node.node_Id}';`;
    },
    getAllNodes: (node) => {
        return `SELECT * FROM "public"."rs_nodes";`;
    },
}

export default node_query;
