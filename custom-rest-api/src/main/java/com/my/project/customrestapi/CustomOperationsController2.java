package com.my.project.customrestapi;

import com.cross_ni.cross.app_int.NodeInterface;
import com.cross_ni.cross.app_int.ProjectInterface;
import com.cross_ni.cross.app_int.SynchroProbeInterface;
import com.cross_ni.cross.app_int.entity.ExternalIdDto;
import com.cross_ni.cross.app_int.entity.NodeDto;
import com.cross_ni.cross.app_int.entity.NodeReference;
import com.cross_ni.cross.app_int.entity.NodeStatusDto;
import com.cross_ni.cross.app_int.entity.SynchroProbeDto;
import com.cross_ni.cross.db.DBFactory;
import com.cross_ni.cross.db.pojo.project.Project;
import com.my.project.customrestapi.model.DatacenterInputDto;
import com.my.project.customrestapi.model.DatacenterOutputDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;

@RestController
public class CustomOperationsController2 {

    @Autowired
    private NodeInterface nodeInterface;

    @Autowired
    private ProjectInterface projectInterface;

    @Autowired
    private SynchroProbeInterface synchroProbeInterface;

    @Autowired
    private DBFactory dbFactory;

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

    @PostMapping(value = "/datacenter", produces = MediaType.APPLICATION_JSON_VALUE)
    public DatacenterOutputDto createDatacenter(@RequestBody DatacenterInputDto dto) {

        // Prepare project
        Project general = dbFactory.findProject("GENERAL");

        // Prepare probe
        String probeName = ExternalIdDto.INTERNAL_CROSS_SYSTEM_ID;
        SynchroProbeDto synchroProbeDto = new SynchroProbeDto();
        synchroProbeDto.setName(probeName);
        synchroProbeDto.setProjectName("GENERAL");
        synchroProbeDto.setUserName("FooUser"); // TODO
        synchroProbeInterface.create(synchroProbeDto);

        // Write to synchro store


        // Execute synchro





    }


}
