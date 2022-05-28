const edge_query = {
    addEdge: (edge) => {
        return `INSERT INTO "public"."rs_edges" 
        ("edge_Id", "startNodeId", "endNodeId", "distance", "weight", "name", "highwayType") 
        VALUES 
        ('${edge.edge_Id}', '${edge.startNodeId}', '${edge.endNodeId}', ${edge.distance}, ${edge.weight}, '${edge.name}', '${edge.highwayType}');`;
    },
    updateEdge: (edge) => {
        return `UPDATE "public"."rs_edges" SET 
                    "startNodeId" = '${edge.startNodeId}', 
                    "endNodeId" = '${edge.endNodeId}', 
                    "distance" = ${edge.distance},
                    "weight" = ${edge.weight},
                    "name" = '${edge.name}', 
                    "highwayType" = '${edge.highwayType}' 
                WHERE "edge_Id" = '${edge.edge_Id}';`;
    },
    deleteEdgeById: (edge) => {
        return `DELETE FROM "public"."rs_edges" 
                WHERE "edge_Id" = '${edge.edge_Id}';`;
    },
    getEdgeById: (edge) => {
        return `SELECT * FROM "public"."rs_edges" 
                WHERE "edge_Id" = '${edge.edge_Id}';`;
    },
    getAllEdges: () => { return `SELECT * FROM "public"."rs_edges";`; },
}

module.exports = edge_query;

