package newandroid.zhongtuobang.com.neoandroiddemo

import android.preference.PreferenceManager
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import newandroid.zhongtuobang.com.neoandroiddemo.api.NEP5Token


data class WatchAddress(val address: String, val nickname: String)
data class Contact(val address: String, val nickname: String)

object PersistentStore {

    fun addWatchAddress(address: String, nickname: String): ArrayList<WatchAddress> {
        val currentAddresses = getWatchAddresses().toCollection(ArrayList<WatchAddress>())
        val toInsert = WatchAddress(address, nickname)
        if (currentAddresses.contains(toInsert)) {
            return currentAddresses
        }

        currentAddresses.add(WatchAddress(address, nickname))
        val gson = Gson()
        val jsonString = gson.toJson(currentAddresses)

        val settingPref = PreferenceManager.getDefaultSharedPreferences(NeoAndroidDemo.appContext).edit()
        settingPref.putString("WATCH_ADDRESSES", jsonString)
        settingPref.apply()

        return currentAddresses
    }

    fun addContact(address: String, nickname: String): ArrayList<Contact> {
        val currentContacts = getContacts().toCollection(ArrayList<Contact>())
        val toInsert = Contact(address, nickname)

        if (currentContacts.contains(toInsert)) {
            return currentContacts
        }
        currentContacts.add(toInsert)
        val gson = Gson()
        val jsonString = gson.toJson(currentContacts)

        val settingPref = PreferenceManager.getDefaultSharedPreferences(NeoAndroidDemo.appContext).edit()
        settingPref.putString("CONTACTS", jsonString)
        settingPref.apply()

        return currentContacts
    }

    fun removeContact(address: String, nickname: String): ArrayList<Contact> {
        val currentContacts = getContacts().toCollection(ArrayList<Contact>())
        currentContacts.remove(Contact(address, nickname))
        val gson = Gson()
        val jsonString = gson.toJson(currentContacts)

        val settingPref = PreferenceManager.getDefaultSharedPreferences(NeoAndroidDemo.appContext).edit()
        settingPref.putString("CONTACTS", jsonString)
        settingPref.apply()

        return currentContacts
    }

    fun removeWatchAddress(address: String, nickname: String): ArrayList<WatchAddress> {
        val currentWatchAddresses = getWatchAddresses().toCollection(ArrayList<WatchAddress>())
        currentWatchAddresses.remove(WatchAddress(address, nickname))
        val gson = Gson()
        val jsonString = gson.toJson(currentWatchAddresses)

        val settingPref = PreferenceManager.getDefaultSharedPreferences(NeoAndroidDemo.appContext).edit()
        settingPref.putString("WATCH_ADDRESSES", jsonString)
        settingPref.apply()
        return currentWatchAddresses
    }



    fun getWatchAddresses(): Array<WatchAddress> {
        var jsonString = PreferenceManager.getDefaultSharedPreferences(NeoAndroidDemo.appContext)
                .getString("WATCH_ADDRESSES", null)

        if (jsonString == null) {
            return arrayOf<WatchAddress>()
        }

        val gson = Gson()
        val contacts = gson.fromJson<Array<WatchAddress>>(jsonString)
        return contacts
    }

    fun getContacts(): Array<Contact> {
        var jsonString = PreferenceManager.getDefaultSharedPreferences(NeoAndroidDemo.appContext)
                .getString("CONTACTS", null)

        if (jsonString == null) {
            return arrayOf<Contact>()
        }

        val gson = Gson()
        val contacts = gson.fromJson<Array<Contact>>(jsonString)
        return contacts
    }

    fun setColdStorageVaultAddress(address: String) {
        val settingPref = PreferenceManager.getDefaultSharedPreferences(NeoAndroidDemo.appContext).edit()
        settingPref.putString("COLD_STORAGE_VAULT_ADDRESS", address)
        settingPref.apply()
    }

    fun getColdStorageVaultAddress(): String {
        return PreferenceManager.getDefaultSharedPreferences(NeoAndroidDemo.appContext)
                .getString("COLD_STORAGE_VAULT_ADDRESS", "")
    }

    fun removeColdStorageVaultAddress() {
        setColdStorageVaultAddress("")
    }



    fun setNodeURL(url: String) {
        val settingPref = PreferenceManager.getDefaultSharedPreferences(NeoAndroidDemo.appContext).edit()
        settingPref.putString("NODE_URL", url)
        settingPref.apply()
    }

    fun getNodeURL(): String {
        return  PreferenceManager.getDefaultSharedPreferences(NeoAndroidDemo.appContext)
                .getString("NODE_URL", "http://seed2.neo.org:10332")
    }


    fun getSelectedNEP5Tokens(): HashMap<String,NEP5Token> {
        var jsonString = PreferenceManager.getDefaultSharedPreferences(NeoAndroidDemo.appContext)
                .getString("SELECTED_NEP5_TOKENS", null)

        if (jsonString == null) {
            return HashMap<String,NEP5Token>()
        }

        val gson = Gson()
        val list = gson.fromJson<HashMap<String,NEP5Token>>(jsonString)
        return list
    }

    fun addToken(token: NEP5Token): HashMap<String,NEP5Token> {
        val currentList = getSelectedNEP5Tokens()
        if (currentList[token.assetID] != null) {
            return currentList
        }

        currentList[token.assetID] = token
        val gson = com.google.gson.Gson()
        val jsonString = gson.toJson(currentList)

        val settingPref = PreferenceManager.getDefaultSharedPreferences(NeoAndroidDemo.appContext).edit()
        settingPref.putString("SELECTED_NEP5_TOKENS", jsonString)
        settingPref.apply()

        return currentList
    }

    fun removeToken(token: NEP5Token): HashMap<String,NEP5Token> {
        val currentList = getSelectedNEP5Tokens()
        currentList.remove(token.assetID)
        val gson = Gson()
        val jsonString = gson.toJson(currentList)

        val settingPref = PreferenceManager.getDefaultSharedPreferences(NeoAndroidDemo.appContext).edit()
        settingPref.putString("SELECTED_NEP5_TOKENS", jsonString)
        settingPref.apply()

        return currentList
    }

    fun setColdStorageEnabledStatus(status: Boolean) {
        var settingsPref = PreferenceManager.getDefaultSharedPreferences(NeoAndroidDemo.appContext).edit()
        settingsPref.putBoolean("COLD_STORAGE_STATUS", status)
        settingsPref.apply()
    }

    fun getColdStorageEnabledStatus(): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(NeoAndroidDemo.appContext).
                getBoolean("COLD_STORAGE_STATUS", false)
    }
}