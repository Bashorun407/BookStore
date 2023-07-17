package com.akinnova.bookstoredemo.service.likebook;

import com.akinnova.bookstoredemo.Exception.ApiException;
import com.akinnova.bookstoredemo.dto.likedto.LikeDto;
import com.akinnova.bookstoredemo.entity.LikeBook;
import com.akinnova.bookstoredemo.repository.BookEntityRepository;
import com.akinnova.bookstoredemo.repository.CustomerRepository;
import com.akinnova.bookstoredemo.repository.LikeBookRepository;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import com.akinnova.bookstoredemo.response.ResponseUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeBookServiceImpl implements ILikeBookService {

    private final LikeBookRepository likeBookRepository;
    private final BookEntityRepository bookEntityRepository;
    private final CustomerRepository customerRepository;

    //Class Constructor


    public LikeBookServiceImpl(LikeBookRepository likeBookRepository, BookEntityRepository bookEntityRepository,
                               CustomerRepository customerRepository) {
        this.likeBookRepository = likeBookRepository;
        this.bookEntityRepository = bookEntityRepository;
        this.customerRepository = customerRepository;
    }

    //1) Method to like/or unlike a  book
    @Override
    public ResponseEntity<?> likeBook(LikeDto likeDto) {

        //Check if the book exists
        if(!bookEntityRepository.existsByTitle(likeDto.getTitle())){
            return new ResponseEntity<>("Book by title: " + likeDto.getTitle() + " does not exist.", HttpStatus.NOT_FOUND);
        }

        //Check if the book has been liked before and check for username...if so, change the unlike and vice versa
        if(likeBookRepository.existsByTitle(likeDto.getTitle())){

            //Collect all likes made on that title in a list
            List<LikeBook> titleList= likeBookRepository.findByTitle(likeDto.getTitle()).get();

            //Filter the list to retain like on the title by username...if username has liked/unliked book prior
            LikeBook likeBook = titleList.stream().filter(x -> x.getTitle().equals(likeDto.getTitle())
                            && x.getUsername().equals(likeDto.getUsername()))
                    .findFirst()
                    .map(bookToLike -> {
                        bookToLike.setLikes(likeDto.getLikes());

                        //if the user selects false, reduce total number of likes
                        if (likeDto.getLikes().equals(false))
                            bookToLike.setTotalLikes(bookToLike.getTotalLikes() - 1);

                        //If the user selects true, increase total number of likes
                        if (likeDto.getLikes().equals(true))
                            bookToLike.setTotalLikes(bookToLike.getTotalLikes() + 1);

                        return bookToLike;
                    }).get();

            //Save the change to the repository
            likeBookRepository.save(likeBook);
        }

        //If book doesn't exist in the likebook repository, create a new record for username and book title
        LikeBook likeBook = LikeBook.builder()
                .username(likeDto.getUsername())
                .title(likeDto.getTitle())
                .likeSpecNum(ResponseUtils.generateUniqueIdentifier(5, likeDto.getUsername()))
                .likes(true)
                .totalLikes(Long.sum(0,1))
                .build();

        likeBookRepository.save(likeBook);

        //Save all changes to repository

        return new ResponseEntity<>("Like updated", HttpStatus.ACCEPTED);
    }

    //2) Method to search for Book Likes on book titles
    @Override
    public ResponsePojo<List<LikeBook>> titleLikes(String title) {
        //To check if book with the specified title exists in the book database
        if(!bookEntityRepository.existsByTitle(title)){
            throw new ApiException(String.format("Book by this title: %s does not exist", title));
        }

        //To retrieve all reviews for a book by title
        List<LikeBook> likeBookList = likeBookRepository.findByTitle(title).get();

        ResponsePojo<List<LikeBook>> responsePojo = new ResponsePojo<>();
        responsePojo.setStatusCode(ResponseUtils.FOUND);
        responsePojo.setMessage(String.format("Reviews for %s: ", title));
        responsePojo.setData(likeBookList);
        return responsePojo;
    }

    //3) Method to retrieve all book-likes in the database
    @Override
    public ResponsePojo<List<LikeBook>> allLikes() {

        //To retrieve all reviews in the review database
        List<LikeBook> likeBookList = likeBookRepository.findAll();

        ResponsePojo<List<LikeBook>> responsePojo = new ResponsePojo<>();
        responsePojo.setStatusCode(ResponseUtils.FOUND);
        responsePojo.setMessage("All reviews: ");
        responsePojo.setData(likeBookList);
        return responsePojo;
    }

//
//    //*) Method to generate Like
//    @Override
//    public ResponseEntity<?> likeBook(LikeDto likeDto) {
//
//        //To check if book with the title exists in the book database
//        if(!bookEntityRepository.existsByTitle(likeDto.getTitle())){
//            throw new ApiException(String.format("Book with this title: %s does not exist", likeDto.getTitle()));
//        }
//
//        //To check if user/customer is registered in the database
//        if(!customerRepository.existsByUsername(likeDto.getUsername())){
//            throw new ApiException(String.format("User with username: %s does not exist", likeDto.getUsername()));
//        }
//
//        //Check the repository if there is already a comment by username on the topic
//        List<LikeBook> likeBookList = likeBookRepository.findByTitle(likeDto.getTitle()).get().stream()
//                .filter(x -> x.getUsername().equals(likeDto.getUsername())).collect(Collectors.toList());
//
//        //If user has not reviewed any book prior, create a new review/like for user
//        //(i.e.) User does not exist and book title does not already exist
//        if(likeBookList.isEmpty()){
//            //new review
//            LikeBook review = LikeBook.builder()
//                    .username(likeDto.getUsername())
//                    .title(likeDto.getTitle())
//                    .likes(likeDto.getLikes())
//                    .likeSpecNum(ResponseUtils.generateUniqueIdentifier(5, likeDto.getUsername()))
//                    .build();
//
//            //To save review to database
//            //Saved to repository
//            likeBookRepository.save(review);
//
//            review.setTotalLikes(((long) likeBookRepository.findByTitle(review.getTitle()).get()
//                    .stream().mapToInt(LikeBook::getLikes).sum()));
//
//            //Saved to repository
//            likeBookRepository.save(review);
//        }
//
//
//        //If user has reviewed a book before, retrieve the exact record for that book and edit it
//        //(i.e. Username exists and title exists)
//        else if (!likeBookList.isEmpty()) {
//
//            LikeBook updatedLike = likeBookRepository.findByTitle(likeDto.getTitle()).get().stream()
//                    .filter(x -> x.getUsername().equals(likeDto.getUsername()))
//                    .findFirst()
//                    .map(review -> {
//
//                        //if review.getLike's value is 1, increment setTotalLikes
//                        if (review.getLikes().equals(1)) {
//                            //Set the value of like as the value the likeDto passes
//                            review.setLikes(ResponseUtils.likeFunction(likeDto.getLikes()));
//
//                            //Save the change in the Review Repository
//                            LikeBook savedBook = likeBookRepository.save(review);
//                            //review.setTotalLikes(review.getTotalLikes() + 1);
//
//                            //if total likes is null, assign the value of saved likes
//                            if(review.getTotalLikes() == null)
//                                review.setTotalLikes((long) savedBook.getLikes());
//
//                                //Else, if total likes is not null, sum up total likes
//                            else if (review.getTotalLikes() != null) {
//                                //Set total likes
//                                review.setTotalLikes(((long) likeBookRepository.findByTitle(review.getTitle()).get()
//                                        .stream().mapToInt(LikeBook::getLikes).sum()));
//                            }
//
//                        } else if(review.getLikes().equals(0)) {
//
//                            //Set the value of like as the value the likeDto passes
//                            review.setLikes(ResponseUtils.likeFunction(likeDto.getLikes()));
//
//                            //Save the change in the Review Repository
//                            LikeBook savedBook = likeBookRepository.save(review);
//                            //review.setTotalLikes(review.getTotalLikes() + 1);
//
//                            //if total likes is null, assign the value of saved likes
//                            if(review.getTotalLikes() == null)
//                                review.setTotalLikes((long) savedBook.getLikes());
//
//                                //Else, if total likes is not null, sum up total likes
//                            else if (review.getTotalLikes() != null) {
//                                //Set total likes
//                                review.setTotalLikes(((long) likeBookRepository.findByTitle(review.getTitle()).get()
//                                        .stream().mapToInt(LikeBook::getLikes).sum()) - 1);
//                            }
//                        }
//                        return review;
//                    }).get();
//
//            //Save the change in the Review Repository
//            likeBookRepository.save(updatedLike);
//        }
//
//        return new ResponseEntity<>("Like added", HttpStatus.GONE);
//    }
}
