package com.android.matrimonyapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MainViewModel : ViewModel() {

    private val repository = Repository()
    private val compositeDisposable = CompositeDisposable()
    val memberList = MutableLiveData<ArrayList<Member>>()
    val loading = MutableLiveData<Boolean>()

    fun getMemberList() {
        loading.postValue(true)
        val disposable = object : DisposableSingleObserver<Result>() {
            override fun onSuccess(t: Result) {
                loading.postValue(false)
                memberList.postValue(t.members)
            }

            override fun onError(e: Throwable) {
                loading.postValue(false)
            }
        }
        repository.getMembers().subscribeOn(Schedulers.io())
            .subscribe(disposable)
        compositeDisposable.add(disposable)

    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}