package com.my.project.customrestapi;

import com.cross_ni.cross.app_int.NodeInterface;
import com.cross_ni.cross.app_int.entity.NodeDto;
import com.cross_ni.cross.app_int.entity.NodeReference;
import com.cross_ni.cross.app_int.entity.NodeStatusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;

@RestController
public class CustomOperationsController {

    @Autowired
    private NodeInterface nodeInterface;

    @GetMapping(value = "/node/{nodeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getNode(@PathVariable Long nodeId) {
        Optional<NodeDto> nodeDtoOpt = nodeInterface.find(
                new NodeReference(nodeId),
                NodeDto.NodeFetch.FETCH_NODETYPES,
                NodeDto.NodeFetch.FETCH_STATUS);
        NodeDto dto = nodeDtoOpt.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return Map.of(
                "id", dto.getNodeId(),
                "name", dto.getName(),
                "types", dto.getTypeDiscriminators(),
                "status", dto.getNodeStatus().map(NodeStatusDto::getName).orElse("NOT_INITIALIZED"));
    }

}
