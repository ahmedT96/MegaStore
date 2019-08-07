package com.food.omar.food;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.food.omar.food.Adapter.CategoryAdabter;
import com.food.omar.food.DataBase.DataSource.CartRepository;
import com.food.omar.food.DataBase.DataSource.FavoritRespository;
import com.food.omar.food.DataBase.Local.CartDataSource;
import com.food.omar.food.DataBase.Local.CartDatabase;
import com.food.omar.food.DataBase.Local.FavoritDataSource;
import com.food.omar.food.Model.Category;
import com.food.omar.food.Model.ChackUserResponse;
import com.food.omar.food.Model.Drink;
import com.food.omar.food.Model.User;
import com.food.omar.food.Model.banners;
import com.food.omar.food.Retrofit.IFoodApi;
import com.food.omar.food.Uitls.Common;
import com.food.omar.food.Uitls.ProgressRequest;
import com.food.omar.food.Uitls.UploadCallBack;
import com.ipaulpro.afilechooser.utils.FileUtils;
import com.nex3z.notificationbadge.NotificationBadge;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,UploadCallBack{
    TextView username,phonenumber;
    SliderLayout sliderLayout;
    RecyclerView recyclerCategory;
    NotificationBadge badge;
    ImageView imageCart;
    CircleImageView circleImageView;
    IFoodApi mServise;
    CompositeDisposable compositeDisposable=new CompositeDisposable();
    private static final int PICK_IMAGE_REQUEST=1222;
    private Uri filePath;
SwipeRefreshLayout swipeRefreshLayout;
    private static final int STORAGE_PERMISSION_CODE=1001;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
              //  Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sliderLayout =  findViewById(R.id.slider_image);

        mServise=Common.getAPI();
        recyclerCategory=findViewById(R.id.List_menu);
        swipeRefreshLayout=findViewById(R.id.swip_to_refresh);
        recyclerCategory.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerCategory.setHasFixedSize(true);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View heder=navigationView.getHeaderView(0);
        username=heder.findViewById(R.id.username);
        phonenumber=heder.findViewById(R.id.userphone);
        circleImageView =(CircleImageView)heder.findViewById(R.id.imageusers);

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.currentuser !=null) {
                    chooseImage();
                }
            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                getBannerImage();

                getMenu();
                gettopingList();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getBannerImage();

                getMenu();
                gettopingList();
            }
        });
        getBannerImage();

        getMenu();
       gettopingList();

        initDB();

        checkSessionLogin();
    }

    private void checkSessionLogin() {
        if (AccountKit.getCurrentAccessToken()!=null)
        {
            swipeRefreshLayout.setRefreshing(true);
            AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                @Override
                public void onSuccess(final Account account) {
                    mServise.chackUserResponse(account.getPhoneNumber().toString())
                            .enqueue(new Callback<ChackUserResponse>() {
                                @Override
                                public void onResponse(Call<ChackUserResponse> call, Response<ChackUserResponse> response) {
                                    ChackUserResponse userResponse=response.body();
                                    if (userResponse.isExistes())
                                    {
                                        mServise.getUserInformation(account.getPhoneNumber().toString())
                                                .enqueue(new Callback<User>() {
                                                    @Override
                                                    public void onResponse(Call<User> call, Response<User> response) {
                                                        Common.currentuser=response.body();
                                                        if (Common.currentuser!=null)
                                                            swipeRefreshLayout.setRefreshing(false);

                                                            username.setText(Common.currentuser.getName());
                                                            phonenumber.setText(Common.currentuser.getPhone());
                                                            String imge = Common.BASE_URL + "upload/" + Common.currentuser.getImage();
                                                            // Toast.makeText(this, imge, Toast.LENGTH_SHORT).show();

                                                            Picasso.with(getBaseContext())
                                                                    .load(imge)
                                                                    .into(circleImageView);

                                                    }

                                                    @Override
                                                    public void onFailure(Call<User> call, Throwable t) {
                                                        swipeRefreshLayout.setRefreshing(false);
                                                    }
                                                });
                                    }
                                    else
                                    {
                                       // startActivity(new Intent(Home.this, login.class));
                                        //finish();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ChackUserResponse> call, Throwable t) {

                                }
                            });

                }

                @Override
                public void onError(AccountKitError accountKitError) {

                }
            });
        }
        else {
            AccountKit.logOut();
           // Intent intent=new Intent(Home.this,login.class);
            ////intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //startActivity(intent);
            //finish();
        }
    }

    private void chooseImage() {
        //Intent intent = new Intent();
        //intent.setType("image/*");
       // intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(FileUtils.createGetContentIntent(), "Select a file"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
if (resultCode== Activity.RESULT_OK) {

        if (requestCode == PICK_IMAGE_REQUEST) {
            if (data != null) {
                filePath = data.getData();

                if (filePath != null && !filePath.getPath().isEmpty()) {
                    circleImageView.setImageURI(filePath);
                    uploadFile();
                } else {
                    Toast.makeText(this, "Cannot Upload", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Cannot Upload3", Toast.LENGTH_SHORT).show();
            }
        }
              else
            {
                Toast.makeText(this, "Cannot Upload4", Toast.LENGTH_SHORT).show();
            }

    }  else
    {Toast.makeText(this, "Cannot Upload5", Toast.LENGTH_SHORT).show();}


    }

    private void uploadFile() {
        if (filePath!=null)
        {
            File file=FileUtils.getFile(this,filePath);
            String filename =Common.currentuser.getPhone()+FileUtils.getExtension(file.toString());
            ProgressRequest requestbody=new ProgressRequest(file,this);
            final MultipartBody.Part body =MultipartBody.Part.createFormData("uploaded_file",filename,requestbody);
            final MultipartBody.Part userPhone =MultipartBody.Part.createFormData("phone",Common.currentuser.getPhone());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    mServise.uploasFile(userPhone,body)
                            .enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    Toast.makeText(Home.this, response.body(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Toast.makeText(Home.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }).start();
        }
        else {        Toast.makeText(this, "2 Upload", Toast.LENGTH_SHORT).show();
        }
    }

    private void initDB() {
        Common.cartDatabase= CartDatabase.getInstance(this);
        Common.cartRepository= CartRepository.getInstance(CartDataSource.getInstance(Common.cartDatabase.cartDAO()));
        Common.favoritRespository= FavoritRespository.getInstance(FavoritDataSource.getInstance(Common.cartDatabase.favoritDAO()));

    }

    private void gettopingList() {
        compositeDisposable.add(mServise.getdrink(Common.currenttoping).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Drink>>() {
                    @Override
                    public void accept(List<Drink> drinks) throws Exception {

                        Common.topinglist=drinks;
                    }
                }));
    }



    private void getMenu() {
        compositeDisposable.add(mServise.getMenu().subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Consumer<List<Category>>() {
                                    @Override
                                    public void accept(List<Category> Categorys) throws Exception {
                                        displaymenu(Categorys);
                                    }
                }));
    }

    private void displaymenu(List<Category> categorys) {
        CategoryAdabter adabter=new CategoryAdabter(this,categorys);
        recyclerCategory.setAdapter(adabter);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void getBannerImage() {
        compositeDisposable.add(mServise.getBanners().subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<List<banners>>() {
            @Override
            public void accept(List<banners> banners) throws Exception {
                displayImage(banners);
            }
        }));
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

    private void displayImage(List<banners> banner) {
        HashMap<String,String> bannerMap =new HashMap<>();
        for (banners item:banner)
            bannerMap.put(item.getName(),item.getLink());
        for (String name:bannerMap.keySet())
        {
            TextSliderView textSliderView=new TextSliderView(this);
            textSliderView.description(name)
                    .image(bannerMap.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            sliderLayout.addSlider(textSliderView);
        }
    }


    boolean isbackButtonClicked = false;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (isbackButtonClicked) {
                super.onBackPressed();
                return;
            }
            this.isbackButtonClicked = true;
            Toast.makeText(this, "Please click back again", Toast.LENGTH_SHORT).show();        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_bar, menu);
     //  View itemview=menu.findItem(R.id.cart_menu).getActionView();

        final MenuItem menuItem = menu.findItem(R.id.cart_menu);

        View actionView = MenuItemCompat.getActionView(menuItem);

      // badge= (NotificationBadge) actionView.findViewById(R.id.badgee);
//        imageCart=  actionView.findViewById(R.id.cart_Item);
       ubdataCartCount();
        return true;
    }

    private void ubdataCartCount() {
        if (badge==null)return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Common.cartRepository.countCartItems()==0)
                    badge.setVisibility(View.VISIBLE);
                badge.setText(String.valueOf(Common.cartRepository.countCartItems()));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ubdataCartCount();
        isbackButtonClicked = false;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.cart_menu) {
            startActivity(new Intent(Home.this,CartActivity.class));
            return true;
        }
        else if (id == R.id.search_menu) {
            startActivity(new Intent(Home.this,SearchActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_signOut) {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Exit Application");
            builder.setMessage("Do you went to exit this application ?");
            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AccountKit.logOut();
                    Intent intent=new Intent(Home.this,login.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            });

            builder.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.show();
        }else if (id == R.id.nav_Favorit) {
            startActivity(new Intent(Home.this,FavoriteActivity.class));
    }
        else if (id == R.id.nav_orders) {
            startActivity(new Intent(Home.this,Orders.class));
            Toast.makeText(this, "hhh", Toast.LENGTH_SHORT).show();
        }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onProgressUpdate(int Pertantege) {

    }


}
