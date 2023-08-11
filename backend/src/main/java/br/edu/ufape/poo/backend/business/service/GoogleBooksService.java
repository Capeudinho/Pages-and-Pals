package br.edu.ufape.poo.backend.business.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class GoogleBooksService {
	public Map<String, Object> findByApiId(String apiId) {
		Gson gson = new Gson();
		WebClient webClient = WebClient.create();
		String response = webClient.get().uri("https://www.googleapis.com/books/v1/volumes/" + apiId).retrieve().bodyToMono(String.class).block();
		JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
		String title = "";
		String cover = "";
		List<String> authors = new ArrayList<String>();
		List<String> categories = new ArrayList<String>();
		Map<String, Object> bookBasic = new HashMap<>();
		
		if (jsonObject != null && jsonObject.get("volumeInfo") != null) {
			JsonObject volumeInfo = jsonObject.get("volumeInfo").getAsJsonObject();
			if (volumeInfo.get("title") != null) {
				title = volumeInfo.get("title").getAsString();
			}
			if (volumeInfo.get("imageLinks") != null
					&& volumeInfo.get("imageLinks").getAsJsonObject().get("thumbnail") != null) {
				cover = volumeInfo.get("imageLinks").getAsJsonObject().get("thumbnail").getAsString();
			}
			if (volumeInfo.get("authors") != null) {
				List<?> rawAuthors = gson.fromJson(volumeInfo.get("authors").getAsJsonArray(), ArrayList.class);
				Iterator<?> rawAuthorsIterator = rawAuthors.listIterator();
				while (rawAuthorsIterator.hasNext()) {
					Object rawAuthor = rawAuthorsIterator.next();
					if (rawAuthor instanceof String) {
						authors.add((String) rawAuthor);
					}
				}
			}
			if (volumeInfo.get("categories") != null) {
				List<?> rawCategoryGroups = gson.fromJson(volumeInfo.get("categories").getAsJsonArray(),ArrayList.class);
				Iterator<?> rawCategoryGroupsIterator = rawCategoryGroups.listIterator();
				while (rawCategoryGroupsIterator.hasNext()) {
					Object rawCategoryGroup = rawCategoryGroupsIterator.next();
					if (rawCategoryGroup instanceof String) {
						String[] categoryParts = ((String) rawCategoryGroup).split(" / ");
						for (int index = 0; index < categoryParts.length; index++) {
							if (!categories.contains(categoryParts[index])) {
								categories.add(categoryParts[index]);
							}
						}
					}
				}
			}
		}
		bookBasic.put("title", title);
		bookBasic.put("cover", cover);
		bookBasic.put("authors", authors);
		bookBasic.put("categories", categories);
		return bookBasic;
	}

	public String findCoverByApiId(String id) {
		Gson gson = new Gson();
		WebClient webClient = WebClient.create();
		String response = webClient.get().uri("https://www.googleapis.com/books/v1/volumes/" + id).retrieve().bodyToMono(String.class).block();
		JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
		String cover = "";
		if (jsonObject != null && jsonObject.get("volumeInfo") != null
				&& jsonObject.get("volumeInfo").getAsJsonObject().get("imageLinks") != null
				&& jsonObject.get("imageLinks").getAsJsonObject().get("thumbnail") != null) {
			cover = jsonObject.get("volumeInfo").getAsJsonObject().get("imageLinks").getAsJsonObject().get("thumbnail")
					.getAsString();
		}
		return cover;
	}

	public List<Object> findByAuthor(String author, int maxResults, int startIndex) {
		Gson gson = new Gson();
		WebClient webClient = WebClient.create();
		String response = webClient.get().uri("https://www.googleapis.com/books/v1/volumes?q=inauthor:" + author
				+ "&maxResults=" + maxResults + "&startIndex=" + startIndex).retrieve().bodyToMono(String.class)
				.block();
		JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
		Map<String, Object> bookBasic;
		List<Object> books = new ArrayList<>();
		String title = "";
		String cover = "";
		List<String> authors;
		List<String> categories;

		if (jsonObject != null && jsonObject.get("items") != null) {
			JsonArray items = jsonObject.get("items").getAsJsonArray();
			for (int index = 0; index < items.size(); index++) {
				bookBasic = new HashMap<>();
				authors = new ArrayList<String>();
				categories = new ArrayList<String>();
				if (items.get(index) != null && items.get(index).getAsJsonObject().get("volumeInfo") != null) {
					JsonObject volumeInfo = items.get(index).getAsJsonObject().get("volumeInfo").getAsJsonObject();
					if (volumeInfo.get("title") != null) {
						title = volumeInfo.get("title").getAsString();
						bookBasic.put("title", title);
					}
					if (volumeInfo.get("imageLinks") != null
							&& volumeInfo.get("imageLinks").getAsJsonObject().get("thumbnail") != null) {
						cover = volumeInfo.get("imageLinks").getAsJsonObject().get("thumbnail").getAsString();
						bookBasic.put("cover", cover);
					}
					if (volumeInfo.get("authors") != null) {
						JsonArray rawAuthors = volumeInfo.get("authors").getAsJsonArray();
						for (int authorIndex = 0; authorIndex < rawAuthors.size(); authorIndex++) {
							if (rawAuthors.get(authorIndex) != null) {
								String rawAuthor = rawAuthors.get(authorIndex).getAsString();
								authors.add(rawAuthor);
							}
						}
					}
					bookBasic.put("authors", authors);
					if (volumeInfo.get("categories") != null) {
						JsonArray rawCategories = volumeInfo.get("categories").getAsJsonArray();
						for (int categoryIndex = 0; categoryIndex <  rawCategories.size(); categoryIndex++) {
							if ( rawCategories.get(categoryIndex) != null) {
								String[] categoryParts = rawCategories.get(categoryIndex).getAsString().split(" / ");
								for (int categoryPartsIndex = 0; categoryPartsIndex < categoryParts.length; categoryPartsIndex++) {
									if (!categories.contains(categoryParts[categoryPartsIndex])) {
										categories.add(categoryParts[categoryPartsIndex]);
									}
								}
							}
						}
					}
					bookBasic.put("categories", categories);
				}
				books.add(bookBasic);
			}
		}
		return books;
	}
}
