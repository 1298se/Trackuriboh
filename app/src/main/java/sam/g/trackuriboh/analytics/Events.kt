package sam.g.trackuriboh.analytics

object Events {
    const val ADD_TO_USER_LIST = "add_to_user_list"
    const val DELETE_FROM_USER_LIST = "delete_user_list"
    const val CHANGE_QUANTITY_USER_LIST = "change_quantity_user_list"

    const val RENAME_USER_LIST = "rename_user_list"
    const val DELETE_USER_LIST = "delete_user_list"
    const val CREATE_USER_LIST = "create_user_list"

    const val ACTION_MODE_ON = "action_mode_on"
    const val ACTION_MODE_OFF = "action_mode_off"

    const val CREATE_REMINDER = "create_reminder"
    const val REMINDER_SCHEDULED = "reminder_scheduled"
    const val DELETE_REMINDER = "delete_reminder"
    const val EDIT_REMINDER = "edit_reminder"
    const val REMINDER_NOTIFICATION_DISPLAYED = "reminder_notification_displayed"
    const val REMINDER_RESCHEDULE = "broadcast_permission_change"
    const val ALARM_PERMISSION_REQUESTED = "alarm_permission_requested"

    const val DATABASE_DOWNLOAD_WORKER_START = "database_download_start"
    const val DATABASE_DOWNLOAD_SUCCESS = "database_download_success"

    const val PRICE_SYNC_WORKER_START = "price_sync_start"
    const val PRICE_SYNC_SUCCESS = "price_sync_success"

    const val UPDATE_WORKER_START = "update_worker_start"
    const val UPDATE_WORKER_SUCCESS = "update_worker_success"

    const val UPDATE_CHECK_WORKER_START = "update_check_worker_start"
    const val UPDATE_CHECK_WORKER_SUCCESS = "update_check_worker_success"
}