package com.theatreapp.theatre.services;

import com.theatreapp.theatre.data.dao.*;
import com.theatreapp.theatre.data.entities.*;
import com.theatreapp.theatre.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class ActorService extends DTO {
    private final ActorDAO actorDAO;
    private final ActorGenderDAO actorGenderDAO;
    private final ActorRatingDAO actorRatingDAO;
    private final PlayDAO playDAO;
    private final PictureDAO pictureDAO;
    private final UserDAO userDAO;
    private final TheatreDAO theatreDAO;
    private final NewsDAO newsDAO;

    public Actor findById(long id) {
        return actorDAO.findById(id).get();
    }

    public ActorDTO findByIdDTO(long id) {
        return convertToActorDTO(actorDAO.findById(id).get());
    }

    public List<ActorDTO> findActorByName(String keyword) {
        return actorDAO.findByNameLikeOrBioLike(keyword, keyword)
                .stream()
                .map(this::convertToActorDTO)
                .collect(Collectors.toList());
    }

    public List<ActorDTO> findSearchedActors(ActorDTO actorDTO) {
        Actor actor = checkIfActorIsNull(actorDTO);

        if (actor == null && actorDTO.getActorGender() == null && actorDTO.getTheatre() == null)
            return new ArrayList<>();
        else return actorDAO.findByActorOrActorGenderOrTheatre(actor, actorDTO.getActorGender(), actorDTO.getTheatre())
                .stream()
                .map(this::convertToActorDTO)
                .collect(Collectors.toList());
    }

    public Actor checkIfActorIsNull(ActorDTO actorDTO) {
        if (actorDTO.getId() != 0) {
            return actorDAO.findById(actorDTO.getId()).get();
        }
        return null;
    }

    public List<ActorDTO> getAllActors() {
        return actorDAO.findAll()
                .stream()
                .map(this::convertToActorDTO)
                .collect(Collectors.toList());
    }

    public List<ActorGenderDTO> getAllActorGenders() {
        return actorGenderDAO.findAll()
                .stream()
                .map(this::convertToGenderDTO)
                .collect(Collectors.toList());
    }

    public float calculateRating(ActorDTO actorDTO) {
        List<ActorRating> usersActorRating = actorRatingDAO.findByActor(convertToActor(actorDTO));
        float sumRating = (float) usersActorRating.stream().mapToDouble(ActorRating::getRating).sum();

        return sumRating / usersActorRating.size();
    }

    public float getActorRating(long id) {
        ActorDTO actorDTO = this.findByIdDTO(id);
        float finalResult = this.calculateRating(actorDTO);

        return Float.isNaN(finalResult) ? 0 : finalResult;
    }

    public List<Float> getActorsRating(List<ActorDTO> actors) {
        List<Float> ratings = new ArrayList<>();

        for (ActorDTO actor : actors) {
            float finalResult = this.calculateRating(actor);
            ratings.add(Float.isNaN(finalResult) ? 0 : finalResult);
        }
        return ratings;
    }

    public List<PictureDTO> getActorGallery(long id) {
        Actor actor = this.findById(id);

        return actor.getGallery()
                .stream()
                .map(this::convertToPictureDTO)
                .collect(Collectors.toList());
    }

    public List<PlayDTO> getActorPlays(long id) {
        Actor actor = this.findById(id);

        return playDAO.getActorPlays(actor)
                .stream()
                .map(this::convertToPlayDTO)
                .collect(Collectors.toList());
    }

    public void addOrEditActor(ActorDTO actorDTO) throws Exception {
        Actor actor = this.convertToActor(actorDTO);

        this.checkValidations(actor);

        if(actorDAO.findById(actor.getId()).isPresent()) {
            this.editActor(actor, actorDTO);
        } else {
            this.addActor(actor);
        }

        actor.setAvatar(actorDTO.getGallery().get(0));
        actorDAO.save(actor);
    }

    void addActor(Actor actor) throws Exception {
        this.checkForUsedPictures(actor);
    }

    void editActor(Actor actor, ActorDTO actorDTO) throws Exception {
        if(!(actorDAO.findById(actorDTO.getId()).get().getGallery()).containsAll(actorDTO.getGallery())) {
            this.checkForUsedPictures(actor);
        }
        actor.getGallery().clear();
        actor.setGallery(actorDTO.getGallery());
    }

    void checkValidations(Actor actor) throws Exception {
        if(actor.getGallery().size() != 3) {
            throw new Exception("Моля, добавете точно 3 снимки!");
        }
    }

    void checkForUsedPictures(Actor actor) throws Exception {
        for (Picture picture : actor.getGallery()) {
            if (pictureDAO.findIfTheatrePictureGalleryExists(picture) ||
                    pictureDAO.findIfActorPictureGalleryExists(picture) ||
                    pictureDAO.findIfPlayPictureGalleryExists(picture)) {
                throw new Exception("Моля, изберете снимки, които не са на актьор/пост./театър !");
            }
        }
    }

    public void deleteActor(ActorDTO actorDTO)  {
        Actor actor = this.convertToActor(actorDTO);

        List<Picture> gallery = List.copyOf(actor.getGallery());
        actor.getGallery().clear();
        actor.setAvatar(new Picture());
        actorRatingDAO.removeAllByActor(actor);
        playDAO.removeActor(actor);
        theatreDAO.removeActor(actor);
        newsDAO.removeActorInNews(actor);
        userDAO.removeTargetedActor(actor);
        userDAO.removeFavouriteActor(actor);
        userDAO.removeWatchedActor(actor);
        actorDAO.removeActorById(actor.getId());

        gallery.forEach(pictureDAO::deleteAllByPicture);
    }
}



//    public ArrayList<Play> sortPlaysBySoldTickets(List<Play> allPlays) {
//        return new ArrayList<>();
//    }
//
//    public ArrayList<Play> sortPlaysByFeature(String feature, List<Play> sortedPlaysBySoldTickets, int scheduleSize) {
//        return new ArrayList<>();
//    }
//    public void k() {
//        String MariaSapunjieva = "Мария Сапунджиева";
//        String LeonidYovchev = "Леонид Йовчев";
//        String DeqnDonkov = "Деян Донков";
//        String drama = "драма";
//        String tragediq = "трагедия";
//        String satira = "сатира";
//
//        ArrayList<Play> allPlays = new ArrayList<>();
//       /* allPlays.add(new Play(DeqnDonkov, satira, 40, 30));
//        allPlays.add(new Play(DeqnDonkov, tragediq, 35, 25));
//        allPlays.add(new Play(DeqnDonkov, drama, 30, 20));
//        allPlays.add(new Play(LeonidYovchev, tragediq, 20, 20));
//        allPlays.add(new Play(LeonidYovchev, drama, 15, 15));
//        allPlays.add(new Play(MariaSapunjieva, drama, 5, 20));
//        allPlays.add(new Play(MariaSapunjieva, tragediq, 10, 15));
//        allPlays.add(new Play(LeonidYovchev, satira, 20, 20));
//        allPlays.add(new Play(MariaSapunjieva, satira, 10, 15));*/
//
//        allPlays.add(new Play(DeqnDonkov, satira, 40, 30));
//        allPlays.add(new Play(DeqnDonkov, tragediq, 35, 25));
//        allPlays.add(new Play(DeqnDonkov, drama, 30, 20));
//        allPlays.add(new Play(LeonidYovchev, tragediq, 30, 20));
//        allPlays.add(new Play(LeonidYovchev, drama, 30, 15));
//        allPlays.add(new Play(MariaSapunjieva, drama, 5, 20));
//        allPlays.add(new Play(MariaSapunjieva, tragediq, 10, 15));
//        allPlays.add(new Play(LeonidYovchev, satira, 20, 20));
//        allPlays.add(new Play(MariaSapunjieva, satira, 10, 15));
//
//        int scheduleSize = 5;
//
//        //comparator which compares plays by the primary feature (soldTicketsCount)
//        Comparator<Play> primaryComparator = Comparator.comparing(Play::getSoldTicketsCount).reversed();
//
//        //list of comparators which compares plays by secondary features
//        ArrayList<Comparator<Play>> secondaryComparators = new ArrayList<>();
//        secondaryComparators.add(Comparator.comparing(Play::getActorName));
//        secondaryComparators.add(Comparator.comparing(Play::getGenreName));
//
//        // list of schedules (each schedule is a list of plays) where
//        // for each secondary feature
//        // all plays are sorted: first by primary comparator (soldTicketsCount) and then by current secondary comparator
//        // then the size of all plays is limited by desired schedule size
//        // and after that the max profit of this streamed schedule is calculated
//
//        /*Optional<List<Play>> proposedSchedule =
//                secondaryComparators.stream().map(secondary -> allPlays.stream()
//                                                             .sorted(primaryComparator.thenComparing(secondary))
//                                                             .limit(scheduleSize)
//                                                             .collect(Collectors.toList()))
//                                                             .max(Comparator.comparingDouble(ActorService::calculateProfit));
//                                                             */
//        /*Optional<List<Play>> proposedSchedule =
//                secondaryComparators.stream().map(secondary -> allPlays.stream()
//                        .sorted(primaryComparator.thenComparing(Comparator.comparing(i -> Collections.frequency(allPlays.forEach(x -> x.actorName. ), secondary)).reversed()))
//                        .limit(scheduleSize)
//                        .collect(Collectors.toList()))
//                        .max(Comparator.comparingDouble(ActorService::calculateProfit));
//
//        System.out.println(proposedSchedule.get());
//
//
//            private static double calculateProfit(List<Play> plays) {
//        double profit = 0;
//        for (Play play : plays) {
//            profit += play.getSoldTicketsCount() * play.getTicketPrice();
//        }
//
//        return profit;
//    }
//
//
//    public class Play {
//
//        private String actorName;
//        private String genreName;
//        private int soldTicketsCount;
//        private double ticketPrice;
//
//        public Play(String actorName, String genreName, int soldTicketsCount, double ticketPrice) {
//            this.actorName = actorName;
//            this.genreName = genreName;
//            this.soldTicketsCount = soldTicketsCount;
//            this.ticketPrice = ticketPrice;
//        }
//
//        public String getActorName() {
//            return actorName;
//        }
//
//        public String getGenreName() {
//            return genreName;
//        }
//
//        public int getSoldTicketsCount() {
//            return this.soldTicketsCount;
//        }
//
//        public double getTicketPrice() {
//            return ticketPrice;
//        }
//
//        @Override
//        public String toString() {
//            return "Play{" +
//                    "actorName='" + actorName + '\'' +
//                    ", genreName='" + genreName + '\'' +
//                    ", soldTicketsCount=" + soldTicketsCount +
//                    ", ticketPrice=" + ticketPrice +
//                    '}';
//        }
//    }
//*/
//    }
//public ArrayList<Play> createProgram() {
//    String MariaSapunjieva = "Мария Сапунджиева";
//    String LeonidYovchev = "Леонид Йовчев";
//    String DeqnDonkov = "Деян Донков";
//    String drama = "драма";
//    String tragediq = "трагедия";
//    String satira = "сатира";
//
//    ArrayList<Play> allPlays = new ArrayList<>();
//    allPlays.add(new Play(DeqnDonkov, satira, 40, 30));
//    allPlays.add(new Play(DeqnDonkov, tragediq, 35, 25));
//    allPlays.add(new Play(DeqnDonkov, drama, 30, 20));
//    allPlays.add(new Play(LeonidYovchev, tragediq, 30, 20));
//    allPlays.add(new Play(LeonidYovchev, drama, 30, 15));
//    allPlays.add(new Play(MariaSapunjieva, drama, 5, 20));
//    allPlays.add(new Play(MariaSapunjieva, tragediq, 10, 15));
//    allPlays.add(new Play(LeonidYovchev, satira, 20, 20));
//    allPlays.add(new Play(MariaSapunjieva, satira, 10, 15));
//
//    int scheduleSize = 5;
//
//    Comparator<Play> primaryComparator = Comparator.comparing(Play::getSoldTicketsCount).reversed();
//
//    ArrayList<Comparator<Play>> secondaryComparators = new ArrayList<>();
//    secondaryComparators.add(Comparator.comparing(Play::getActorName));
//    secondaryComparators.add(Comparator.comparing(Play::getGenreName));
//
//    Optional<List<Play>> proposedSchedule =
//            secondaryComparators.stream().map(secondary -> allPlays.stream()
//                    .sorted(Comparator.comparing(i -> Collections.frequency(allPlays, i)).reversed())
//                    .sorted(primaryComparator.thenComparing(secondary))
//                    .limit(scheduleSize)
//                    .collect(Collectors.toList()))
//                    .max(Comparator.comparingDouble(ActorService::calculateProfit));
//
//
//
//    List<Play> sortedBySoldTickets = new ArrayList<>();
//    List<ArrayList<Play>> sortedByFeature = new ArrayList<ArrayList<Play>>();
//
//    List<String> features = Arrays.asList("actor", "genre");
//
//    sortedBySoldTickets = sortPlaysBySoldTickets(allPlays);
//    for(int i = 0; i < features.size(); i++) {
//        sortedByFeature.add(sortPlaysByFeature(features.get(i), sortedBySoldTickets, scheduleSize));
//    }
//    return findBestSchedule(sortedByFeature);
//}
//    public ArrayList<Play> findBestSchedule(List<ArrayList<Play>> schedules) {
//        List<ArrayList<Play>> sortedBySoldTickets = new ArrayList<ArrayList<Play>>();
//
//        return sortedBySoldTickets.get(0);
//    }
