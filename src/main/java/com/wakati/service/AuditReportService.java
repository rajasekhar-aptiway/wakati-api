package com.wakati.service;

import com.wakati.model.request.AuditReportRequest;
import com.wakati.repository.AuditReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AuditReportService {

    @Autowired
    private AuditReportRepository repo;

    public Map<String, Object> generateReport(AuditReportRequest req) {

        String reportType = Optional.ofNullable(req.getReportType()).orElse("").toLowerCase();

        resolveDates(req);

        return switch (reportType) {

            case "user_creation" -> userCreationReport(req);

            case "users_by_role" -> usersByRoleReport(req);

            case "users_by_status" -> usersByStatusReport(req);

            case "wallet_to_wallet" -> walletToWalletReport(req);

            case "withdrawals" -> withdrawalsReport(req);

            case "deposits" -> depositsReport(req);

            case "commissions" -> commissionsReport(req);

            case "platform_overview" -> platformOverview(req);

            default -> Map.of("400", "Invalid report_type");
        };
    }

    private void resolveDates(AuditReportRequest req) {

        if (req.getFromDate() == null && req.getToDate() == null) {

            LocalDate now = LocalDate.now();

            switch (req.getPeriod()) {
                case "day" -> {
                    req.setFromDate(now.toString());
                    req.setToDate(now.toString());
                }
                case "week" -> {
                    req.setFromDate(now.minusDays(6).toString());
                    req.setToDate(now.toString());
                }
                case "month" -> {
                    req.setFromDate(now.minusDays(29).toString());
                    req.setToDate(now.toString());
                }
            }
        }
    }

    private Map<String, Object> usersByRoleReport(AuditReportRequest req) {

        List<Map<String, Object>> data =
                repo.usersByRoleSummary(
                        req.getFromDate(),
                        req.getToDate()
                );

        return Map.of(
                        "report_type", "users_by_role",
                        "records", data
                );
    }

    private Map<String, Object> usersByStatusReport(AuditReportRequest req) {

        List<Map<String, Object>> data =
                repo.usersByStatusSummary(
                        req.getFromDate(),
                        req.getToDate()
                );

        return Map.of(
                        "report_type", "users_by_status",
                        "records", data
                );
    }

    private Map<String, Object> withdrawalsReport(AuditReportRequest req) {

        int offset = (req.getPage() - 1) * req.getPageSize();

        long total = repo.withdrawalCount(
                req.getUserId(),
                req.getFromDate(),
                req.getToDate()
        );

        List<Map<String, Object>> rows =
                repo.withdrawalRecords(
                        req.getUserId(),
                        req.getFromDate(),
                        req.getToDate(),
                        req.getPageSize(),
                        offset
                );

        return
                Map.of(
                        "total", total,
                        "records", rows,
                        "pagination", paginate(total, req.getPage(), req.getPageSize())
                );
    }

    private Map<String, Object> depositsReport(AuditReportRequest req) {

        int offset = (req.getPage() - 1) * req.getPageSize();

        long total = repo.depositCount(
                req.getUserId(),
                req.getFromDate(),
                req.getToDate()
        );

        List<Map<String, Object>> rows =
                repo.depositRecords(
                        req.getUserId(),
                        req.getFromDate(),
                        req.getToDate(),
                        req.getPageSize(),
                        offset
                );

        return
                Map.of(
                        "total", total,
                        "records", rows,
                        "pagination", paginate(total, req.getPage(), req.getPageSize())
                );
    }

    private Map<String, Object> platformOverview(AuditReportRequest req) {

        Map<String, Object> data =
                repo.platformOverview(
                        req.getFromDate(),
                        req.getToDate()
                );

        return
                Map.of(
                        "report_type", "platform_overview",
                        "data", data
                );
    }

    private Map<String, Object> userCreationReport(AuditReportRequest req) {

        int offset = (req.getPage() - 1) * req.getPageSize();

        List<Map<String, Object>> summary =
                repo.userCreationSummary(req);

        long total = repo.userCreationCount(req.getUserType(),req.getStatus(),req.getUserId(),req.getFromDate(),req.getToDate());

        List<Map<String, Object>> rows =
                repo.userCreationRecords(req.getUserType(),req.getStatus(),req.getUserId(),req.getFromDate(),req.getToDate(),req.getPageSize(), offset);

        Map<String, Object> data = Map.of(
                "report_type", "user_creation",
                "filters", Map.of(
                        "from_date", req.getFromDate(),
                        "to_date", req.getToDate(),
                        "status", req.getStatus(),
                        "user_type", req.getUserType(),
                        "created_by", req.getUserId()
                ),
                "summary", summary,
                "pagination", paginate(total, req.getPage(), req.getPageSize()),
                "records", rows
        );

        return data;
    }

    private Map<String,Object> walletToWalletReport(AuditReportRequest req) {

        String callerType = repo.getUserType(req.getUserId());

        String effectiveChannel;

        if (List.of("ADMIN", "CFO").contains(callerType)) {
            effectiveChannel = req.getChannel();
        } else {
            effectiveChannel = callerType;

            if (req.getChannel() != null &&
                    !req.getChannel().equalsIgnoreCase(callerType)) {

                throw new RuntimeException("Access denied");
            }
        }

        int offset = (req.getPage() - 1) * req.getPageSize();

        long total = repo.walletTxnCount(req.getUserId(),effectiveChannel,req.getFromDate(),req.getToDate());

        List<Map<String, Object>> rows =
                repo.walletTxnRecords(req.getUserId(), effectiveChannel,req.getFromDate(),req.getToDate(),req.getPageSize(), offset);

        return Map.of(
                        "total", total,
                        "records", rows
                );
    }

    private Map<String, Object> commissionsReport(AuditReportRequest req) {

        int offset = (req.getPage() - 1) * req.getPageSize();

        Map<String, Object> agg = repo.commissionAggregate(req.getUserId(),req.getFromDate(),req.getToDate());

        List<Map<String, Object>> summary = repo.commissionSummary(req.getUserId(),req.getFromDate(),req.getToDate());

        List<Map<String, Object>> rows =
                repo.commissionRecords(req.getUserId(),req.getFromDate(),req.getToDate(),req.getPageSize(), offset);

        return Map.of(
                        "total_receipts", agg.get("total_receipts"),
                        "total_received", agg.get("total_received"),
                        "summary", summary,
                        "records", rows
                );
    }

    private Map<String, Object> paginate(long total, int page, int size) {

        int totalPages = total > 0 ? (int) Math.ceil((double) total / size) : 0;

        return Map.of(
                "page", page,
                "page_size", size,
                "total", total,
                "total_pages", totalPages
        );
    }
}