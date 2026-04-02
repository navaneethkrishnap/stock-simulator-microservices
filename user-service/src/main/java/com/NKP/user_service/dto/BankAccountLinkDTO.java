package com.NKP.user_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BankAccountLinkDTO {
    @NotBlank(message = "Please enter bank name")
    private String bankName;
    @NotBlank(message = "Please enter account number properly")
    @Pattern(regexp = "^[0-9]{9,18}$", message = "Account number must be 9-18 digits")
    private String accountNumber;
    @NotBlank(message = "Please enter account name as per bank records properly")
    private String accountName;
    @NotBlank(message = "Please enter branch name properly")
    private String branchName;
    @NotBlank(message = "Please enter ifsc number properly")
    @Pattern(regexp = "^[A-Z]{4}0[A-Z0-9]{6}$", message = "Invalid IFSC format")
    private String ifsc;
    @NotBlank(message = "Please enter aadhaar number properly")
    @Pattern(regexp = "^[0-9]{12}$", message = "Aadhaar must be 12 digits")
    private String aadhaarNumber;
    @NotBlank(message = "Please enter pan number properly")
    @Pattern(regexp = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$", message = "Invalid PAN format")
    private String panNumber;
}
