package gov.va.api.lighthouse.veteranverification.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(
    name = "branch_of_service",
    description = "military branch of service",
    example = "Air Force")
public class BranchOfService {}
