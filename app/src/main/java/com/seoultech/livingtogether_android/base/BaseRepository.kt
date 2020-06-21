package com.seoultech.livingtogether_android.base

import android.util.Log

abstract class BaseRepository<T> {
    protected val TAG = javaClass.simpleName

    protected open fun insert(entity: T) {
        Log.d(TAG, "Room insert : $entity")
    }

    protected open fun delete(entity: T) {
        Log.d(TAG, "Room delete : $entity")
    }

    protected open fun update(entity: T) {
        Log.d(TAG, "Room update : $entity")
    }
}