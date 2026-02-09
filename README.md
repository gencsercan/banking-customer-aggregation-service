# banking-customer-aggregation-service

**Backend aggregation service** inspired by real enterprise banking systems.

This project demonstrates how customer-related data is aggregated from **multiple distributed systems**
under **latency, timeout, and partial-failure conditions** — a common challenge in large-scale financial institutions.

---

## Why this project exists
In real banking environments:
- Customer data lives in multiple systems
- Some systems are slow or temporarily unavailable
- Backend services must respond **gracefully**, not fail hard

This service shows:
- Parallel service calls (fan-out / fan-in)
- Timeout handling per dependency
- Partial responses with clear source-level status
- Clean orchestration logic suitable for senior-level backend roles

---

## Architecture (conceptual)

```
Client
  |
  v
CustomerProfileController
  |
  v
CustomerProfileService
  |—— CoreBankingClient
  |—— MarketingClient
  |—— CampaignStatsClient
```

Each downstream system is called **in parallel**.
Failures are isolated and reported without breaking the overall response.

---

## Endpoint

```
GET /customers/{customerId}/profile
```

### Example response (shortened)

```json
{
  "customerId": "123",
  "coreBanking": { "branchCode": "0459", "status": "ACTIVE" },
  "marketing": { "contactable": true },
  "campaignStats": null,
  "warnings": {
    "campaign-stats": "timeout"
  }
}
```

---

- **WebClient + reactive types** for parallel calls
- Explicit **timeouts per dependency**
- Error isolation instead of cascading failures
- No database to keep focus on orchestration logic
- Clear separation of controller / service / client layers

---

## Run locally

```bash
mvn spring-boot:run
curl http://localhost:8080/customers/123/profile
```

### Simulate failures / latency
Edit `application.yml`:
- `app.simulate.*DelayMs`
- `app.simulate.*Fail`

---

## Docker

```bash
docker build -t customer-aggregation:0.1 .
docker run -p 8080:8080 customer-aggregation:0.1
```

---

