package br.edu.ufape.poo.backend.business.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class GoogleBooksService
{
	public String findCoverByApiId(String id)
	{
		Gson gson = new Gson();
		WebClient webClient = WebClient.create();
		String response = webClient.get().uri("https://www.googleapis.com/books/v1/volumes/"+id).retrieve().bodyToMono(String.class).block();
		JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
		String cover = "";
		if
		(
			jsonObject != null &&
			jsonObject.get("volumeInfo") != null &&
			jsonObject.get("volumeInfo").getAsJsonObject().get("imageLinks") != null &&
			jsonObject.get("volumeInfo").getAsJsonObject().get("imageLinks").getAsJsonObject().get("thumbnail") != null
		)
		{
			cover = jsonObject.get("volumeInfo").getAsJsonObject().get("imageLinks").getAsJsonObject().get("thumbnail").getAsString();
		}
		return cover;
	}
	
	public Map<String, Object> findBasicByApiId(String apiId)
	{
		Gson gson = new Gson();
		WebClient webClient = WebClient.create();
		String response = webClient.get().uri("https://www.googleapis.com/books/v1/volumes/"+apiId).retrieve().bodyToMono(String.class).block();
		JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
		String title = "";
		String cover = "";
		List<String> authors = new ArrayList<String>();
		List<String> categories = new ArrayList<String>();
		Map<String, Object> bookBasicInformation = new HashMap<>();
		if (jsonObject != null && jsonObject.get("volumeInfo") != null)
		{
			JsonObject volumeInfo = jsonObject.get("volumeInfo").getAsJsonObject();
			if (volumeInfo.get("title") != null)
			{
				title = volumeInfo.get("title").getAsString();
			}
			if (volumeInfo.get("imageLinks") != null && volumeInfo.get("imageLinks").getAsJsonObject().get("thumbnail") != null)
			{
				cover = volumeInfo.get("imageLinks").getAsJsonObject().get("thumbnail").getAsString();
			}
			if (volumeInfo.get("authors") != null)
			{
				List<?> rawAuthors = gson.fromJson(volumeInfo.get("authors").getAsJsonArray(), ArrayList.class);
				Iterator<?> rawAuthorsIterator = rawAuthors.listIterator();
				while (rawAuthorsIterator.hasNext())
				{
					Object rawAuthor = rawAuthorsIterator.next();
					if (rawAuthor instanceof String)
					{
						authors.add((String) rawAuthor);
					}
				}
			}
			if (volumeInfo.get("categories") != null)
			{
				List<?> rawCategoryGroups = gson.fromJson(volumeInfo.get("categories").getAsJsonArray(), ArrayList.class);
				Iterator<?> rawCategoryGroupsIterator = rawCategoryGroups.listIterator();
				while (rawCategoryGroupsIterator.hasNext())
				{
					Object rawCategoryGroup = rawCategoryGroupsIterator.next();
					if (rawCategoryGroup instanceof String)
					{
						String[] categoryParts = ((String) rawCategoryGroup).split(" / ");
						for (int index = 0; index < categoryParts.length; index++)
						{
							if (!categories.contains(categoryParts[index]))
							{
								categories.add(categoryParts[index]);
							}
						}
					}
				}
			}
		}
		bookBasicInformation.put("title", title);
		bookBasicInformation.put("cover", cover);
		bookBasicInformation.put("authors", authors);
		bookBasicInformation.put("categories", categories);
		return bookBasicInformation;
	}
}