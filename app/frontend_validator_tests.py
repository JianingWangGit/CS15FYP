import pandas as pd

# Define the frontend white-box test summary (one row per major tested component)
data = [
    ["AuthInputValidator", "F1.1", "Email and password format validation", "Passed"],
    ["GiveReviewActivity", "F1.2a–i", "Validator and internal logic paths (rating, ID, login, submission)", "Passed"],
    ["OpeningHoursValidator", "F1.3", "24-hour format validation for opening/closing times", "Passed"],
    ["RestaurantFilterUtils", "F1.4", "Filter logic: price, cuisine, both, no match", "Passed"],
    ["RestaurantInputValidator", "F1.5", "Required name, desc, cuisine, address fields", "Passed"],
    ["ReviewInputValidator", "F1.6", "Rating, comment length, restaurant ID validity", "Passed"]
]

# Create DataFrame
df = pd.DataFrame(data, columns=["Component", "Test ID", "Scenario / Path Type", "Status"])

# Save to CSV inside app/test-reports/
df.to_csv("app/test-reports/frontend_validator_tests.csv", index=False)

print("✅ CSV saved to app/test-reports/frontend_validator_tests.csv")
