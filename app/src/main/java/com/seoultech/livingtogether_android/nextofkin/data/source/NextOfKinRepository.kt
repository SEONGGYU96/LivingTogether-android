package com.seoultech.livingtogether_android.nextofkin.data.source

import com.seoultech.livingtogether_android.nextofkin.data.NextOfKin


class NextOfKinRepository(private val nextOfKinLocalDataSource: NextOfKinDataSource) :
    NextOfKinDataSource {

    private val cachedNextOfKin = LinkedHashMap<String, NextOfKin>()

    private var cacheIsDirty = false

    override fun getNextOfKin(callback: NextOfKinDataSource.LoadNextOfKinCallback) {
        if (cachedNextOfKin.isNotEmpty() && !cacheIsDirty) {
            callback.onNextOfKinLoaded(ArrayList(cachedNextOfKin.values))
            return
        }

        nextOfKinLocalDataSource.getNextOfKin(object :
            NextOfKinDataSource.LoadNextOfKinCallback {
            override fun onNextOfKinLoaded(nextOfKin: List<NextOfKin>) {
                refreshCache(nextOfKin)
                callback.onNextOfKinLoaded(nextOfKin)
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }
        })
    }

    override fun getNextOfKin(phoneNumber: String, callback: NextOfKinDataSource.GetNextOfKinCallback) {
        val nextOfKinInCache = getNextOfKinByPhoneNumber(phoneNumber)

        if (nextOfKinInCache != null) {
            callback.onNextOfKinLoaded(nextOfKinInCache)
            return
        }

        nextOfKinLocalDataSource.getNextOfKin(phoneNumber, object :
            NextOfKinDataSource.GetNextOfKinCallback {
            override fun onNextOfKinLoaded(nextOfKin: NextOfKin) {
                cacheAndPerform(nextOfKin) {
                    callback.onNextOfKinLoaded(it)
                }
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }
        })
    }

    override fun saveNextOfKin(nextOfKin: NextOfKin) {
        cacheAndPerform(nextOfKin) {
            nextOfKinLocalDataSource.saveNextOfKin(it)
        }
    }

    override fun deleteNextOfKin(phoneNumber: String) {
        nextOfKinLocalDataSource.deleteNextOfKin(phoneNumber)
        cachedNextOfKin.remove(phoneNumber)
    }

    private fun getNextOfKinByPhoneNumber(phoneNumber: String) = cachedNextOfKin[phoneNumber]

    private fun refreshCache(nextOfKin: List<NextOfKin>) {
        cachedNextOfKin.clear()
        nextOfKin.forEach {
            cacheAndPerform(it) {}
        }
        cacheIsDirty = false
    }

    private inline fun cacheAndPerform(nextOfKin: NextOfKin, perform: (NextOfKin) -> Unit) {
        val cacheNextOfKin = NextOfKin(
            nextOfKin.name,
            nextOfKin.phoneNumber
        )
        cachedNextOfKin[cacheNextOfKin.phoneNumber] = cacheNextOfKin
        perform(cacheNextOfKin)
    }

    companion object {

        private var INSTANCE: NextOfKinRepository? = null

        @JvmStatic fun getInstance(nextOfKinLocalDataSource: NextOfKinDataSource) =
            INSTANCE
                ?: synchronized(NextOfKinRepository::class.java) {
                INSTANCE
                    ?: NextOfKinRepository(
                        nextOfKinLocalDataSource
                    )
                    .also { INSTANCE = it }
            }
    }
}