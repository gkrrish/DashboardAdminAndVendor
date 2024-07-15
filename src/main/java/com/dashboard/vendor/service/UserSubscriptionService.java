package com.dashboard.vendor.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dashboard.entity.BatchJob;
import com.dashboard.entity.Mandal;
import com.dashboard.entity.MasterNewspaper;
import com.dashboard.entity.StatewiseLocation;
import com.dashboard.entity.UserDetails;
import com.dashboard.entity.UserSubscription;
import com.dashboard.entity.UserSubscriptionId;
import com.dashboard.entity.Vendor;
import com.dashboard.vendor.repository.BatchJobRepository;
import com.dashboard.vendor.repository.MandalRepository;
import com.dashboard.vendor.repository.MasterNewspaperRepository;
import com.dashboard.vendor.repository.StateRepository;
import com.dashboard.vendor.repository.StatewiseLocationRepository;
import com.dashboard.vendor.repository.UserDetailsRepository;
import com.dashboard.vendor.repository.UserSubscriptionRepository;
import com.dashboard.vendor.repository.VendorRepository;

import jakarta.transaction.Transactional;

@Service
public class UserSubscriptionService {
	
	@Autowired
	private VendorRepository vendorRepository;
	@Autowired
	private StateRepository stateRepository;
	@Autowired
	private MasterNewspaperRepository masterNewspaperRepository;
	@Autowired
    private UserDetailsRepository userDetailsRepository;
    @Autowired
    private MandalRepository mandalRepository;
    @Autowired
    private StatewiseLocationRepository statewiseLocationRepository;
    @Autowired
    private BatchJobRepository batchJobRepository;
    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;
    
    
	
	public List<Object[]> getDistinctStatesByLanguage(String languageName) {
        return vendorRepository.findDistinctStatesByLanguage(languageName);
    }
	
	public List<Object[]> getDistinctNewspapersByStateName(String stateName) {
        return masterNewspaperRepository.findDistinctNewspapersByStateName(stateName);
    }
	
	public List<Object[]> getDistinctStatesByNewspaperName(String newspaperName) {
        return stateRepository.findDistinctStatesByNewspaperName(newspaperName);
    }
	
	
	@Transactional
    public void upsertUserSubscription(String mobileNumber, String batchTime, String mandalName, String newspaperName, boolean unSubscribe) {
        Optional<UserDetails> userDetailsOptional = userDetailsRepository.findByMobileNumber(mobileNumber);
        UserDetails user = userDetailsOptional.orElseThrow(() -> new RuntimeException("User not found"));
        Long userid = user.getUserid();

        Mandal mandal = mandalRepository.findByMandalName(mandalName).orElseThrow(() -> new RuntimeException("Mandal not found"));
        Long mandalId = mandal.getMandalId();

        StatewiseLocation location = statewiseLocationRepository.findByMandal_MandalId(mandalId).orElseThrow(() -> new RuntimeException("Location not found"));
        Long locationId = location.getLocationId();

        MasterNewspaper newspaper = masterNewspaperRepository.findByNewspaperName(newspaperName).orElseThrow(() -> new RuntimeException("Newspaper not found"));
        Long newspaperMasterId = newspaper.getId();

        BatchJob batchJob = batchJobRepository.findByDeliveryTime(batchTime).orElseThrow(() -> new RuntimeException("Batch not found"));
        @SuppressWarnings("unused")
		Long batchId = batchJob.getBatchId();

		Vendor vendor = vendorRepository.findVendorsByMasterIdAndLocationId(newspaperMasterId, locationId)
													.orElseThrow(() -> new RuntimeException("Vendor not found! Currently at this location/Mandal: "+ mandalName + " newspaper not present."));
		Long newspaperId = vendor.getId().getNewspaperId();
		
		//NOTE : Vendor table should be updated through ADMINSTRATOR of this application/Marketing team. not the user nor anybody. 
		//because Vendor table has the information of which are the papers are published by the vendors on which location those things will be taken from official resources.
		//for any worst case scenario force full update of the vendor and User-subscription details 
		if(unSubscribe==true) {
			deleteUserSubscription(userid, newspaperId);
		}else {
        saveOrUpdateUserSubscription(userid, newspaperId, user, vendor, batchJob);
		}
    }
	
	
	
	//if these inputs are kept in cache then directly call this method, while user selecting the options try to keep in cache to reduce the calls to database.
	public void saveOrUpdateUserSubscription(Long userId, Long newspaperId, UserDetails user, Vendor vendor, BatchJob batchJob) {
	    Optional<UserSubscription> existingSubscription = userSubscriptionRepository.findByUserIdAndNewspaperId(userId, newspaperId);

	    if (existingSubscription.isPresent()) {
	        UserSubscription subscription = existingSubscription.get();
	        subscription.setBatch(batchJob);
	        userSubscriptionRepository.save(subscription);
	    } else {
	        UserSubscription newSubscription = new UserSubscription();
	        newSubscription.setUserDetails(user);
	        newSubscription.setVendor(vendor);
	        newSubscription.setBatch(batchJob);

	        UserSubscriptionId userSubscriptionId = new UserSubscriptionId();
	        userSubscriptionId.setUser(user);
	        userSubscriptionId.setVendor(vendor);
	        newSubscription.setId(userSubscriptionId);

	        userSubscriptionRepository.save(newSubscription);
	    }
	}

	public void deleteUserSubscription(Long userId, Long newspaperId) {
        Optional<UserSubscription> existingSubscription = userSubscriptionRepository.findByUserIdAndNewspaperId(userId, newspaperId);
        
        existingSubscription.ifPresent(subscription -> {
            userSubscriptionRepository.delete(subscription);
        });
    }
	
	  public Optional<Vendor> getVendorsByNewspaperMasterIdAndLocationId(Long newspaperMasterId, Long locationId) {
	        return vendorRepository.findVendorsByMasterIdAndLocationId(newspaperMasterId, locationId);
	    }
}
