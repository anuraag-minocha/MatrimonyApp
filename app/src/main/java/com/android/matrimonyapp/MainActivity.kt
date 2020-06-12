package com.android.matrimonyapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: MainViewModel
    lateinit var memberRecyclerAdapter: MemberRecyclerAdapter
    lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build()

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        setupView()
        setUpObserver()

    }

    private fun setupView() {
        memberRecyclerAdapter = MemberRecyclerAdapter(arrayListOf(), this, db)
        rv.layoutManager = LinearLayoutManager(this@MainActivity)
        rv.adapter = memberRecyclerAdapter

        checkDb()

    }

    private fun setUpObserver() {

        viewModel.memberList.observe(this, Observer {
            if (it.isNotEmpty()) {
                insertAllInDb(it)
            }
        })


        viewModel.loading.observe(this, Observer {
            if (it) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        })

    }

    fun checkDb() {
        val checkDbObservable: Single<Boolean> = Single.create { emitter ->
            try {
                if (db.memberDao().getAll().isEmpty())
                    emitter.onSuccess(true)
                else
                    emitter.onSuccess(false)
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
        val disposable = object : DisposableSingleObserver<Boolean>() {
            override fun onSuccess(t: Boolean) {
                if (t)
                    viewModel.getMemberList()
                else
                    loadFromDb()
            }

            override fun onError(e: Throwable) {
            }
        }
        checkDbObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(disposable)
    }

    fun loadFromDb() {
        val dbObservable: Single<ArrayList<MemberData>> = Single.create { emitter ->
            try {
                emitter.onSuccess(db.memberDao().getAll() as ArrayList<MemberData>)
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
        val disposable = object : DisposableSingleObserver<ArrayList<MemberData>>() {
            override fun onSuccess(t: ArrayList<MemberData>) {
                memberRecyclerAdapter.updateList(t)
            }

            override fun onError(e: Throwable) {
            }
        }
        dbObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(disposable)

    }


    fun insertAllInDb(list: ArrayList<Member>) {
        val dbObservable: Single<ArrayList<MemberData>> = Single.create { emitter ->
            try {
                for ((index, member) in list.withIndex()) {
                    db.memberDao().insertAll(
                        MemberData(
                            index,
                            member.name.first + member.name.last.substring(0, 1),
                            member.picture.medium,
                            member.location.city + ", " + member.location.state,
                            member.dob.age.toString(),
                            "none"
                        )
                    )
                }
                emitter.onSuccess(db.memberDao().getAll() as ArrayList<MemberData>)
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
        val disposable = object : DisposableSingleObserver<ArrayList<MemberData>>() {
            override fun onSuccess(t: ArrayList<MemberData>) {
                memberRecyclerAdapter.updateList(t)
            }

            override fun onError(e: Throwable) {
            }
        }
        dbObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(disposable)

    }


}