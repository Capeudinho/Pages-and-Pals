package br.edu.ufape.poo.backend.business.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import br.edu.ufape.poo.backend.exceptions.BookNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class GoogleBooksService implements GoogleBooksServiceInterface {
	public Map<String, Object> findByApiId(String apiId, String extractInfo) throws Exception{
		Gson gson = new Gson();
		WebClient webClient = WebClient.create();
		String response;
		try {
			response = webClient.get().uri("https://www.googleapis.com/books/v1/volumes/" + apiId).retrieve()
				.bodyToMono(String.class).block();
		}
		catch (Exception exception)
		{
			throw new BookNotFoundException();	
		}
		JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
		bookInfo = extractBookInformationUtility(jsonObject, extractInfo);
		if (bookInfo == null)
		{
			throw new BookNotFoundException();
		}
		return bookInfo;
	}

	public String findCoverByApiId(String id) {
		Gson gson = new Gson();
		WebClient webClient = WebClient.create();
		String response = "";
		try
		{
			response = webClient.get().uri("https://www.googleapis.com/books/v1/volumes/" + id).retrieve()
				.bodyToMono(String.class).block();
		}
		catch(Exception e)
		{
			response = "";
		}
		JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
		String cover = "";
		if (jsonObject != null && jsonObject.get("volumeInfo") != null
				&& jsonObject.get("volumeInfo").getAsJsonObject().get("imageLinks") != null
				&& jsonObject.get("volumeInfo").getAsJsonObject().get("imageLinks").getAsJsonObject()
						.get("thumbnail") != null) {
			cover = jsonObject.get("volumeInfo").getAsJsonObject().get("imageLinks").getAsJsonObject().get("thumbnail")
					.getAsString();
		}
		return cover;
	}

	public List<Object> advancedSearchResults(String term, String title, String author, String subject,String publisher, String isbn, Integer maxResults, Integer startIndex, String extractInfo) {
		Map<String, Object> preview = new HashMap<>();
		List<Object> previews = new ArrayList<Object>();
		String response = advancedSearchUtility(term, title, author, subject, publisher, isbn, maxResults, startIndex);
		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(response, JsonObject.class);

		if (jsonObject != null && jsonObject.get("items") != null) {
			JsonArray items = jsonObject.get("items").getAsJsonArray();
			for (int index = 0; index < items.size(); index++) {
				JsonObject bookObject = items.get(index).getAsJsonObject();
				preview = extractBookInformationUtility(bookObject, extractInfo);
				previews.add(preview);
			}
		}
		return previews;
	}

	private String advancedSearchUtility(String term, String title, String author, String subject, String publisher,String isbn, Integer maxResults, Integer startIndex) {
		String filter = "";
		String filters = "";
		String response = "";
		WebClient webClient = WebClient.create();
		if (term != null && !term.isBlank()) {
			filter = filter + term + "&";
		}
		if (title != null && !title.isBlank()) {
			filter = filter + "intitle:" + title + "&";
		}
		if (author != null && !author.isBlank()) {
			filter = filter + "inauthor:" + author + "&";
		}

		if (subject != null && !subject.isBlank()) {
			filter = filter + "subject:" + subject + "&";
		}
		if (publisher != null && !publisher.isBlank()) {
			filter = filter + "inpublisher:" + publisher + "&";

		}
		if (isbn != null && !isbn.isBlank()) {
			filter = filter + "isbn:" + isbn + "&";
		}
		if (maxResults != null) {
			if (maxResults < 1) {
				maxResults = 1;
			}
			filter = filter + "maxResults=" + maxResults + "&";
		}
		if (startIndex != null) {
			if (startIndex < 0) {
				startIndex = 0;
			}
			filter = filter + "startIndex=" + startIndex + "&";
		}
		
		if (filter.length() > 0) {
			filters = filter.substring(0, filter.length() - 1);
		}
		try
		{
			response = webClient.get().uri("https://www.googleapis.com/books/v1/volumes?q=" + filters).retrieve().bodyToMono(String.class).block();
		}
		catch(Exception e)
		{
			response = "";
		}

		return response;
	}

	private Map<String, Object> extractBookInformationUtility(JsonObject jsonObject, String extractAllData) {
		Map<String, Object> bookInfo;
		String title = "";
		String cover = "";
		String language = "";
		String publisher = "";
		String publishedDate = "";
		String description = "";
		String identifier = "";
		String type = "";
		int pageCount = 0;
		List<String> authors;
		List<String> categories;
		List<Map<String, Object>> industryIdentifiers;
		Map<String, Object> industryId;
		bookInfo = new HashMap<>();
		authors = new ArrayList<String>();
		categories = new ArrayList<String>();
		industryIdentifiers = new ArrayList<Map<String, Object>>();

		if (jsonObject.get("volumeInfo") != null) {
			JsonObject volumeInfo = jsonObject.get("volumeInfo").getAsJsonObject();

			if (volumeInfo.get("title") != null) {
				title = volumeInfo.get("title").getAsString();
				bookInfo.put("title", title);
			}
			if (volumeInfo.get("categories") != null) {
				JsonArray rawCategories = volumeInfo.get("categories").getAsJsonArray();
				for (int categoryIndex = 0; categoryIndex < rawCategories.size(); categoryIndex++) {
					if (rawCategories.get(categoryIndex) != null) {
						String[] categoryParts = rawCategories.get(categoryIndex).getAsString().split(" / ");
						for (int categoryPartsIndex = 0; categoryPartsIndex < categoryParts.length; categoryPartsIndex++) {
							if (!categories.contains(categoryParts[categoryPartsIndex])) {
								categories.add(categoryParts[categoryPartsIndex]);
							}
						}
					}
				}
				bookInfo.put("categories", categories);
			}

			if (volumeInfo.get("publishedDate") != null) {
				publishedDate = volumeInfo.get("publishedDate").getAsString();
				bookInfo.put("publishedDate", publishedDate);
			}

			if (volumeInfo.get("imageLinks") != null
					&& volumeInfo.get("imageLinks").getAsJsonObject().get("thumbnail") != null) {
				cover = volumeInfo.get("imageLinks").getAsJsonObject().get("thumbnail").getAsString();
				bookInfo.put("cover", cover);
			}
			if (volumeInfo.get("authors") != null) {
				JsonArray rawAuthors = volumeInfo.get("authors").getAsJsonArray();
				for (int authorIndex = 0; authorIndex < rawAuthors.size(); authorIndex++) {
					if (rawAuthors.get(authorIndex) != null) {
						String rawAuthor = rawAuthors.get(authorIndex).getAsString();
						authors.add(rawAuthor);
					}
				}
				bookInfo.put("authors", authors);
			}

			if ("complete".equals(extractAllData)) {
				if (volumeInfo.get("pageCount") != null) {
					pageCount = volumeInfo.get("pageCount").getAsInt();
					bookInfo.put("pageCount", pageCount);
				}
				if (volumeInfo.get("language") != null) {
					language = volumeInfo.get("language").getAsString();
					bookInfo.put("language", language);
				}
				if (volumeInfo.get("publisher") != null) {
					publisher = volumeInfo.get("publisher").getAsString();
					bookInfo.put("publisher", publisher);
				}
				if (volumeInfo.get("description") != null) {
					description = volumeInfo.get("description").getAsString();
					bookInfo.put("description", description);
				}

				if (volumeInfo.get("industryIdentifiers").getAsJsonArray() != null) {
					JsonArray rawIndustryIdentifiers = volumeInfo.get("industryIdentifiers").getAsJsonArray();
					for (int industryIdentifiersIndex = 0; industryIdentifiersIndex < rawIndustryIdentifiers
							.size(); industryIdentifiersIndex++) {
						industryId = new HashMap<>();
						if (rawIndustryIdentifiers.get(industryIdentifiersIndex) != null) {
							JsonObject rawIndustryIdentifier = rawIndustryIdentifiers.get(industryIdentifiersIndex)
									.getAsJsonObject();
							if (rawIndustryIdentifier.get("type") != null) {
								type = rawIndustryIdentifier.get("type").getAsString();
								industryId.put("type", type);
							}
							if (rawIndustryIdentifier.get("identifier") != null) {
								identifier = rawIndustryIdentifier.get("identifier").getAsString();
								industryId.put("identifier", identifier);
							}
							industryIdentifiers.add(industryId);
						}
					}
					bookInfo.put("ISBNs", industryIdentifiers);
				}
			}
		}
		if (bookInfo.isEmpty())
		{
			bookInfo = null;
		}
		return bookInfo;
	}
}
