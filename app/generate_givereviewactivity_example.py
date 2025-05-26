import pandas as pd
from pathlib import Path

# Refined white-box test descriptions for validator logic in GiveReviewActivity
data = [
    ["GiveReviewActivity", "F1.2a", "Rating = 4.5 (typical valid input)", "Passed"],
    ["GiveReviewActivity", "F1.2b", "Rating = 0.0 (invalid: below minimum threshold)", "Passed"],
    ["GiveReviewActivity", "F1.2c", "Rating = 0.5 (boundary valid: minimum acceptable)", "Passed"],
    ["GiveReviewActivity", "F1.2d", "Rating = 5.5 (invalid: exceeds maximum allowed)", "Passed"],
    ["GiveReviewActivity", "F1.2e", "Restaurant ID = '123' (non-empty, valid)", "Passed"],
    ["GiveReviewActivity", "F1.2f", "Restaurant ID = '' (invalid: empty string)", "Passed"],
    ["GiveReviewActivity", "F1.2g", "Restaurant ID = null (invalid: not provided)", "Passed"],
    ["GiveReviewActivity", "F1.2h", "Restaurant ID = whitespace only (invalid)", "Passed"],
    ["GiveReviewActivity", "F1.2i", "User not logged in (FirebaseUser is null)", "Passed"],
]

# Create DataFrame
df = pd.DataFrame(data, columns=["Component", "Test ID", "Scenario / Path Type", "Status"])

# Save to CSV
csv_path = Path("app/test-reports/frontend_givereviewactivity_example.csv")
csv_path.parent.mkdir(parents=True, exist_ok=True)
df.to_csv(csv_path, index=False)

print(f"✅ CSV saved to {csv_path}")
