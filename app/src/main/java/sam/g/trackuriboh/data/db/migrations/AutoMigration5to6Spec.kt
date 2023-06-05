package sam.g.trackuriboh.data.db.migrations

import androidx.room.DeleteColumn
import androidx.room.migration.AutoMigrationSpec

@DeleteColumn.Entries(
    DeleteColumn(tableName = "Inventory", columnName = "avgPurchasePrice"),
    DeleteColumn(tableName = "Inventory", columnName = "totalRealizedProfit"),
    DeleteColumn(tableName = "Inventory", columnName = "quantity")
)
class AutoMigration5to6Spec : AutoMigrationSpec