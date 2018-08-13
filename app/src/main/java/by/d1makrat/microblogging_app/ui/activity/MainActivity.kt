package by.d1makrat.microblogging_app.ui.activity

import android.app.Activity
import android.app.FragmentManager
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.view.View
import by.d1makrat.microblogging_app.R
import by.d1makrat.microblogging_app.R.id.*
import by.d1makrat.microblogging_app.R.string.*
import by.d1makrat.microblogging_app.presenter.activity.MainPresenter
import by.d1makrat.microblogging_app.ui.fragment.AddFragment
import by.d1makrat.microblogging_app.ui.fragment.AllPostsFragment
import by.d1makrat.microblogging_app.ui.fragment.HomeFragment
import by.d1makrat.microblogging_app.ui.fragment.ProfileFragment
import by.d1makrat.microblogging_app.workers.FirebaseAuthenticationWorker
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity: Activity(), NavigationView.OnNavigationItemSelectedListener, MainPresenter.View {

    private val manager: FragmentManager = fragmentManager
    private val mainPresenter = MainPresenter()

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        mainPresenter.attachView(this)

        floatingActionButton.setOnClickListener{
            manager.beginTransaction().replace(R.id.content_main, AddFragment()).commit()
            toolbar.title = getString(nav_add)
            floatingActionButton.visibility = View.GONE
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, navigation_drawer_open, navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)

        manager.beginTransaction().replace(R.id.content_main, HomeFragment()).commit()
        toolbar?.title = getString(nav_home)
    }

    override fun onStart() {
        super.onStart()

        mainPresenter.getUserInfo()
    }

    override fun onDestroy() {
        mainPresenter.detachView()
        super.onDestroy()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        floatingActionButton.visibility = View.VISIBLE

        when (item.itemId){
            all_posts -> {
                manager.beginTransaction().replace(R.id.content_main, AllPostsFragment()).commit()
                toolbar?.title = getString(nav_all_posts)
            }
            home ->{
                manager.beginTransaction().replace(R.id.content_main, HomeFragment()).commit()
                toolbar.title = getString(nav_home)
            }
            add ->{
                manager.beginTransaction().replace(R.id.content_main, AddFragment()).commit()
                toolbar?.title = getString(nav_add)
                floatingActionButton.visibility = View.GONE
            }
            profile ->{
                manager.beginTransaction().replace(R.id.content_main, ProfileFragment()).commit()
                toolbar?.title = getString(nav_profile)
            }
            logout ->{
                FirebaseAuthenticationWorker().signOut()
                startSignInScreen()
                finish()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun startSignInScreen(){
        startActivity(Intent(this, SignInActivity::class.java))
    }

    override fun showUserInfoInHeader(userName: String, userSurname: String, userMail: String) {
        textView_header_title.text = "$userName $userSurname"
        textView_header_subtitle.text = userMail
    }
}
