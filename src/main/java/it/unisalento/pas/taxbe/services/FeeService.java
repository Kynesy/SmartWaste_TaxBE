/**
 * Service class responsible for handling fee-related operations.
 * This service provides methods for creating, deleting, and managing fees.
 */
package it.unisalento.pas.taxbe.services;

import it.unisalento.pas.taxbe.domains.Fee;
import it.unisalento.pas.taxbe.domains.WasteStatistics;
import it.unisalento.pas.taxbe.repositories.IFeeRepository;
import it.unisalento.pas.taxbe.utils.FeeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing fees in the application.
 */
@Service
public class FeeService implements IFeeService {

    private final IFeeRepository feeRepository;

    /**
     * Constructor for FeeService.
     *
     * @param feeRepository The repository for accessing fee-related data.
     */
    @Autowired
    public FeeService(IFeeRepository feeRepository) {
        this.feeRepository = feeRepository;
    }

    /**
     * Creates a new fee.
     *
     * @param feeToAdd The fee object to be added to the repository.
     * @return 0 if the fee is successfully created, otherwise an error code.
     */
    @Override
    public int createFee(Fee feeToAdd) {
        feeRepository.save(feeToAdd);
        return 0;
    }

    /**
     * Deletes a fee with the specified ID.
     *
     * @param feeId The ID of the fee to be deleted.
     * @return 0 if the fee is successfully deleted, 1 if the fee does not exist.
     */
    @Override
    public int deleteFee(String feeId) {
        if (feeRepository.existsById(feeId)) {
            feeRepository.deleteById(feeId);
            return 0;
        }
        return 1;
    }

    /**
     * Marks a fee as paid.
     *
     * @param feeId The ID of the fee to be marked as paid.
     * @return 0 if the fee is successfully marked as paid, 1 if the fee does not exist.
     */
    @Override
    public int payFee(String feeId) {
        Optional<Fee> optFee = feeRepository.findById(feeId);
        if (optFee.isPresent()) {
            Fee feeToBePaid = optFee.get();
            feeToBePaid.setPaid(1);
            feeRepository.save(feeToBePaid);
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * Retrieves all fees associated with a specific user ID.
     *
     * @param userId The user ID for which to retrieve fees.
     * @return An ArrayList of Fee objects associated with the user.
     */
    @Override
    public ArrayList<Fee> getAllFeeByUserID(String userId) {
        List<Fee> feeList = feeRepository.findAllByUserId(userId);
        return new ArrayList<>(feeList);
    }

    /**
     * Retrieves all fees in the system.
     *
     * @return An ArrayList of all Fee objects in the system.
     */
    @Override
    public ArrayList<Fee> getAllFees() {
        List<Fee> feeList = feeRepository.findAll();
        return new ArrayList<>(feeList);
    }
}
