package com.rbiffi.vacationfriend.AppSections.Home;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.rbiffi.vacationfriend.AppSections.Home.ViewModels.VacationViewModel;
import com.rbiffi.vacationfriend.AppSections.Itinerario.FragmentRoute;
import com.rbiffi.vacationfriend.AppSections.Liste.FragmentLists;
import com.rbiffi.vacationfriend.AppSections.Spese.FragmentExpenses;
import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Participant;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Vacation;
import com.rbiffi.vacationfriend.Repository.VacationFriendRepository;
import com.rbiffi.vacationfriend.Utils.ActivityNavigateAppObj;
import com.rbiffi.vacationfriend.Utils.Constants;
import com.rbiffi.vacationfriend.Utils.Converters;

import java.io.InputStream;
import java.util.List;

// classe che contiene e gestisce i frammenti collegati alla navigazione primaria dell'app
public class ActivityVacation
        extends ActivityNavigateAppObj
        implements
        VacationFriendRepository.IRepositoryListener {

    private VacationViewModel viewModel;

    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView vacationPhoto;
    private BottomNavigationView bottomNavigationView;

    private FragmentManager fm;
    private Fragment activeFragment;

    private FragmentHome fHome;
    private FragmentRoute fRoute;
    private FragmentExpenses fExpences;
    private FragmentLists fLists;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = getActivityViewModel();
        saveDataFromIntentMaybe(savedInstanceState);
        restoreState(savedInstanceState);

        setupBottomNavigation();
    }

    @Override
    protected void setActivityContentView() {
        setContentView(R.layout.activity_navigate_app);
    }

    private VacationViewModel getActivityViewModel() {
        return ViewModelProviders.of(this).get(VacationViewModel.class);
    }

    private void saveDataFromIntentMaybe(final Bundle savedInstanceState) {
        Intent intent = getIntent();
        // Nel parcel ci sono tutti i dati della vacanza, ma a me basta mostrare subito foto e titolo
        Vacation current = intent.getParcelableExtra(Constants.PARCEL_SELECTED_VACATION);
        if (current != null) {
            // eseguito subito e solo 1 volta alla creazione della activity
            saveImmediateNeedingDataFromParcel(current);
            setupTitleAndPicture();

            viewModel.getCurrentVacation().observe(this, new Observer<Vacation>() {
                @Override
                public void onChanged(@Nullable Vacation vacation) {
                    if (vacation != null) {
                        viewModel.setFieldTitle(vacation.title);
                        viewModel.setFieldPeriodFrom(Converters.dateToUserInterface(vacation.period.startDate));
                        viewModel.setFieldPeriodTo(Converters.dateToUserInterface(vacation.period.endDate));
                        viewModel.setFieldPlace(vacation.place);
                        viewModel.getCurrentParticipants().observe(ActivityVacation.this, new Observer<List<Participant>>() {
                            @Override
                            public void onChanged(@Nullable List<Participant> participants) {
                                viewModel.setParticipants(participants);

                                // tutti i dati della home pronti, posso caricare la view
                                if (fm == null)
                                    setupFragmentsAndStartHome(savedInstanceState);
                            }
                        });
                        viewModel.setFieldPhoto(vacation.photo);

                        setupTitleAndPicture();
                    }
                }
            });

        }
    }

    private void saveImmediateNeedingDataFromParcel(Vacation current) {
        viewModel.setVacationId(current.id);
        viewModel.setFieldTitle(current.title);
        viewModel.setFieldPhoto(current.photo);
    }

    private void restoreState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            String title = savedInstanceState.getString(Constants.IN_TITLE);
            if (title != null) viewModel.setFieldTitle(title);

            String dateFrom = savedInstanceState.getString(Constants.IN_PERIODFROM);
            if (dateFrom != null) viewModel.setFieldPeriodFrom(dateFrom);

            String dateTo = savedInstanceState.getString(Constants.IN_PERIODTO);
            if (dateTo != null) viewModel.setFieldPeriodTo(dateTo);

            String place = savedInstanceState.getString(Constants.IN_PLACE);
            if (place != null) viewModel.setFieldPlace(place);

            String photo = savedInstanceState.getString(Constants.IN_PHOTO);
            if (photo != null) viewModel.setFieldPhoto(Uri.parse(photo));

            //todo altri campi da ripristinare, devo considerarli tutti
        }
        // else leggo tutto dal view model
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // salvo il frammento attualmente visibile
        fm.putFragment(outState, "myFragmentName", activeFragment);
    }

    private void setupTitleAndPicture() {
        collapsingToolbar = findViewById(R.id.collapsingToolbar);
        collapsingToolbar.setTitle(viewModel.getFieldTitle());
        vacationPhoto = findViewById(R.id.vacation_collapsing_image);

        try {
            InputStream inputStream = getContentResolver().openInputStream(viewModel.getFieldPhoto());
            if (inputStream.available() <= 2097152) { // immagine più piccola di 2MB
                Drawable userImage = Drawable.createFromStream(inputStream, viewModel.getFieldPhoto().toString());
                vacationPhoto.setBackground(userImage);
            } else {
                int compressionFactor = inputStream.available() <= 4194304 ? 2 : 4;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = compressionFactor; // comprimo di un fattore di 2 o 4 l'immagine
                Bitmap imageBit = BitmapFactory.decodeStream(inputStream, null, options);
                Drawable userImage = new BitmapDrawable(getResources(), imageBit);
                vacationPhoto.setBackground(userImage);
            }
        } catch (Exception e) {
            Toast.makeText(this, R.string.err_photo_not_found, Toast.LENGTH_SHORT).show();
        }

    }

    private void setupFragmentsAndStartHome(Bundle savedInstanceState) {
        fm = getSupportFragmentManager();

        // apro la home se prima apertura della activity
        if (isFirstCreationActivity(savedInstanceState)) {
            FragmentHome fHome = createFragmentsAndAddToManager();
            showFragment(fHome);
        } else {
            activeFragment = fm.getFragment(savedInstanceState, "myFragmentName");
        }
    }

    @NonNull
    private FragmentHome createFragmentsAndAddToManager() {
        fHome = new FragmentHome();
        fRoute = new FragmentRoute();
        fExpences = new FragmentExpenses();
        fLists = new FragmentLists();

        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.navigation_fragment_container, fHome, Constants.FTAG_HOME).hide(fHome);
        ft.add(R.id.navigation_fragment_container, fRoute, Constants.FTAG_ROUTE).hide(fRoute);
        ft.add(R.id.navigation_fragment_container, fExpences, Constants.FTAG_EXPENSES).hide(fExpences);
        ft.add(R.id.navigation_fragment_container, fLists, Constants.FTAG_LISTS).hide(fLists);
        ft.commit();

        return fHome;
    }

    private boolean isFirstCreationActivity(Bundle savedInstanceState) {
        return savedInstanceState == null;
    }

    private void setupBottomNavigation() {
        bottomNavigationView = findViewById(R.id.navigation_bottom);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                supportInvalidateOptionsMenu();
                switch (item.getItemId()) {
                    case R.id.actionHome:
                        Fragment fHome = fm.findFragmentByTag(Constants.FTAG_HOME);
                        assert fHome != null;
                        showFragment(fHome);
                        break;

                    case R.id.actionItinerario:
                        Fragment fRoute = fm.findFragmentByTag(Constants.FTAG_ROUTE);
                        assert fRoute != null;
                        showFragment(fRoute);
                        break;

                    case R.id.actionSpese:
                        Fragment fExpenses = fm.findFragmentByTag(Constants.FTAG_EXPENSES);
                        showFragment(fExpenses);
                        break;

                    case R.id.actionListe:
                        Fragment fLists = fm.findFragmentByTag(Constants.FTAG_LISTS);
                        showFragment(fLists);
                        break;

                    default:
                        return false;
                }

                return true;
            }
        });

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                // todo scroll all'inizio della schermata, ma anche nothing va bene
            }
        });
    }

    private void showFragment(@NonNull Fragment fragment) {
        FragmentTransaction tr = fm.beginTransaction();
        tr.show(fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        if (activeFragment != null)
            tr.hide(activeFragment);

        tr.commit();
        activeFragment = fragment;
    }

    @NonNull
    @Override
    protected Toolbar getToolbarView() {
        return findViewById(R.id.toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent) || isTaskRoot()) {
                    // This activity is NOT part of this app's task, so create a new task
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                            // Navigate up to the closest parent
                            .startActivities();
                } else {
                    // This activity is part of this app's task
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onVacationOperationComplete(long rowId) {

    }

}
















