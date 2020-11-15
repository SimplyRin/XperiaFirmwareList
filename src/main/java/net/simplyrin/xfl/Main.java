package net.simplyrin.xfl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

/**
 * Created by SimplyRin on 2020/11/15.
 *
 * Copyright (c) 2020 SimplyRin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
public class Main {

	public static void main(String[] args) {
		new Main().run();
	}

	public void run() {
		System.out.println("XperiFirm でダウンロードしたファームウェアを JSON 形式で出力します。");

		Scanner scanner = new Scanner(System.in);

		String path = null;
		while (true) {
			System.out.print("パスを入力してください: ");
			path = scanner.nextLine();
			if (path == null || !path.equals("")) {
				break;
			}
		}

		scanner.close();

		HashMap<String, Device> devices = new HashMap<>();

		File file = new File(path);
		for (File folder : file.listFiles()) {
			String name = folder.getName();
			if (!folder.isDirectory() || name.contains("\\") || !name.contains("_") || !name.contains("-")) {
				continue;
			}

			String model = name.split("_")[0];
			String operator = name.split("_")[1];
			String build = name.split("_")[2].split("-")[0];

			try {
				Integer.valueOf(build);
				continue;
			} catch (Exception e) {
			}

			if (devices.get(model) == null) {
				devices.put(model, new Device(model));
			}

			devices.get(model).addBuild(operator, build);
			System.out.println(model + ", " + operator + ", " + build);
		}

		Object[] object = devices.keySet().toArray();
		Arrays.sort(object);

		List<Device> list = new ArrayList<>();
		for (Object key : object) {
			list.add(devices.get((String) key));
		}

		Gson gson = new Gson();
		String json = gson.toJson(list);

		String result = new GsonBuilder().setPrettyPrinting().create().toJson(JsonParser.parseString(json));
		System.out.println(result);
	}

}
