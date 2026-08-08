package com.yourwatchrental.watchrental.watch.dto.request;

import com.yourwatchrental.watchrental.branch.Branch;

import java.util.UUID;

public record WatchBranchUpdateRequestDTO(
        UUID branchId
) {
}
