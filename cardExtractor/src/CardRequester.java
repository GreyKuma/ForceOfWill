import model.CardRaw;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CardRequester {
    public List<CardRaw> request (CardRequest request) throws IOException {
        List<CardRaw> retList = new ArrayList<>();

        Document doc = Jsoup.parse(new URL(request.toUrl()).openStream(), "UTF-8", request.toUrl()); /// Jsoup.connect(request.toUrl()).get();
        Element head_en = doc.select("#right_dettcarta #inglese").first();
        Element head_de = doc.select("#right_dettcarta #local").first();

        Element main = doc.selectFirst("#left_dettcarta .tit_carta").parent();
        main.children().forEach(child -> {
            if (child.is("#left_dettcarta .tit_carta")) {
                CardRaw card = new CardRaw();
                card.idNumeric = request.id;

                Element id = main.selectFirst("#left_dettcarta .tit_carta");
                if (id != null) {
                    card.idStr = id.text();
                }

                Element img = main.selectFirst("#left_dettcarta img");
                if (img != null) {
                    card.imageUrl = img.attr("src");
                }

                if (head_en != null && head_de != null) {
                    Element last_data_en = head_en.selectFirst("p").children().first(), cur_data_en = last_data_en;
                    Element last_data_de = head_de.selectFirst("p").children().first(), cur_data_de = last_data_de;

                    do {
                        if (cur_data_en.text().toLowerCase().startsWith("name")) {
                            card.name.en = (cur_data_en = cur_data_en.nextElementSibling()).text();
                        } else if (cur_data_en.text().toLowerCase().startsWith("edition")) {
                            card.edition.en = (cur_data_en = cur_data_en.nextElementSibling()).text();
                        } else if (cur_data_en.text().toLowerCase().startsWith("type")) {
                            card.type.en = (cur_data_en = cur_data_en.nextElementSibling()).text();
                        } else if (cur_data_en.text().toLowerCase().startsWith("race")) {
                            card.race.en = (cur_data_en = cur_data_en.nextElementSibling()).text();
                        } else if (cur_data_en.text().toLowerCase().startsWith("cost")) {
                            card.cost = (cur_data_en = cur_data_en.nextElementSibling()).html();
                        } else if (cur_data_en.text().toLowerCase().startsWith("atk")) {
                            card.atk = Integer.parseInt((cur_data_en = cur_data_en.nextElementSibling()).text());
                        } else if (cur_data_en.text().toLowerCase().startsWith("def")) {
                            card.def = Integer.parseInt((cur_data_en = cur_data_en.nextElementSibling()).text());
                        } else if (cur_data_en.text().toLowerCase().startsWith("attributeIds")) {
                            card.attribute.en = (cur_data_en = cur_data_en.nextElementSibling()).text();
                        } else if (cur_data_en.text().toLowerCase().startsWith("rarity")) {
                            card.rarity = (cur_data_en = cur_data_en.nextElementSibling()).text();
                        } else if (cur_data_en.text().toLowerCase().startsWith("ability")) {
                            cur_data_en = cur_data_en.parent().nextElementSibling().children().first();
                            do {
                                if (card.ability.en.length() > 0 && cur_data_en.html().length() > 0) {
                                    card.ability.en += "\n";
                                }
                                card.ability.en += cur_data_en.html();
                            } while (
                                (
                                    cur_data_en.nextElementSibling() != null &&
                                    !cur_data_en.nextElementSibling().text().toLowerCase().contains("flavor") &&
                                    (cur_data_en = cur_data_en.nextElementSibling()) != null
                                ) || (
                                    cur_data_en.parent().nextElementSibling() != null &&
                                    !cur_data_en.parent().nextElementSibling().text().toLowerCase().contains("flavor") &&
                                    (cur_data_en = cur_data_en.parent().nextElementSibling().children().first()) != null
                                )
                            );

                            /*img = (cur_data_en = cur_data_en.parent().nextElementSibling()).selectFirst("img");
                            if (img != null) {
                                ret.ability.en += "<IMG>" + img.attr("src") + "</IMG>";
                            }
                            ret.ability.en += cur_data_en.text();
                            while (
                                    cur_data_en.nextElementSibling() != null &&
                                    !cur_data_en.nextElementSibling().text().toLowerCase().contains("flavor")) {
                                img = (cur_data_en = cur_data_en.nextElementSibling()).selectFirst("img");
                                if (img != null) {
                                    ret.ability.en += "\n<IMG>" + img.attr("src") + "</IMG>\n";
                                } else {
                                    ret.ability.en += "\n";
                                }
                                ret.ability.en += cur_data_en.text();
                            }
                            cur_data_en = cur_data_en.children().last();*/
                        } else if (cur_data_en.text().toLowerCase().startsWith("flavor")) {
                            card.flavor.en = (cur_data_en = cur_data_en.parent().nextElementSibling().children().last()).text();
                            while (cur_data_en.parent().nextElementSibling() != null) {
                                card.flavor.en += "\n" + (cur_data_en = cur_data_en.parent().nextElementSibling()).text();
                            }
                        }

                        last_data_en = cur_data_en;
                    } while (
                        (cur_data_en = cur_data_en.nextElementSibling()) != null || (
                            last_data_en.parent().nextElementSibling() != null && (
                                (
                                    last_data_en.parent().nextElementSibling().children().size() == 0 &&
                                    last_data_en.parent().nextElementSibling().nextElementSibling() != null &&
                                    (cur_data_en = last_data_en.parent().nextElementSibling().nextElementSibling().children().first()) != null
                                ) || (
                                    last_data_en.parent().nextElementSibling() != null &&
                                    (cur_data_en = last_data_en.parent().nextElementSibling().children().first()) != null
                                )
                            )
                        )
                    );

                    do {
                        if (cur_data_de.text().toLowerCase().startsWith("kartenname")) {
                            card.name.de = (cur_data_de = cur_data_de.nextElementSibling()).text();
                        } else if (cur_data_de.text().toLowerCase().startsWith("set")) {
                            card.edition.de = (cur_data_de = cur_data_de.nextElementSibling()).text();
                        } else if (cur_data_de.text().toLowerCase().startsWith("kartentyp")) {
                            card.type.de = (cur_data_de = cur_data_de.nextElementSibling()).text();
                        } else if (cur_data_de.text().toLowerCase().startsWith("eigenschaften")) {
                            card.race.de = (cur_data_de = cur_data_de.nextElementSibling()).text();
                        } else if (cur_data_de.text().toLowerCase().startsWith("kosten")) {
                            card.cost = (cur_data_de = cur_data_de.nextElementSibling()).html();
                        } else if (cur_data_de.text().toLowerCase().startsWith("atk")) {
                            card.atk = Integer.parseInt((cur_data_de = cur_data_de.nextElementSibling()).text());
                        } else if (cur_data_de.text().toLowerCase().startsWith("def")) {
                            card.def = Integer.parseInt((cur_data_de = cur_data_de.nextElementSibling()).text());
                        } else if (cur_data_de.text().toLowerCase().startsWith("attribut")) {
                            card.attribute.de = (cur_data_de = cur_data_de.nextElementSibling()).text();
                        } else if (cur_data_de.text().toLowerCase().startsWith("seltenheit")) {
                            card.rarity = (cur_data_de = cur_data_de.nextElementSibling()).text();
                        } else if (cur_data_de.text().toLowerCase().startsWith("fähigkeit")) {
                            cur_data_de = cur_data_de.parent().nextElementSibling().children().first();
                            do {
                                if (card.ability.de.length() > 0 && cur_data_de.html().length() > 0) {
                                    card.ability.de += "\n";
                                }
                                card.ability.de += cur_data_de.html();
                            } while (
                                (
                                    cur_data_de.nextElementSibling() != null &&
                                    !cur_data_de.nextElementSibling().text().toLowerCase().contains("stimmungstext") &&
                                    (cur_data_de = cur_data_de.nextElementSibling()) != null
                                ) || (
                                    cur_data_de.parent().nextElementSibling() != null &&
                                    !cur_data_de.parent().nextElementSibling().text().toLowerCase().contains("stimmungstext") &&
                                    (cur_data_de = cur_data_de.parent().nextElementSibling().children().first()) != null
                                )
                            );
                        } else if (cur_data_de.text().toLowerCase().startsWith("stimmungstext")) {
                            card.flavor.de = (cur_data_de = cur_data_de.parent().nextElementSibling()).text();
                            while (cur_data_de.nextElementSibling() != null) {
                                card.flavor.de += "\n" + (cur_data_de = cur_data_de.nextElementSibling()).text();
                            }
                            cur_data_de = cur_data_de.children().first();
                        }

                        last_data_de = cur_data_de;
                    } while (
                        (cur_data_de = cur_data_de.nextElementSibling()) != null || (
                            last_data_de.parent().nextElementSibling() != null && (
                                (
                                    last_data_de.parent().nextElementSibling().children().size() == 0 &&
                                    last_data_de.parent().nextElementSibling().nextElementSibling() != null &&
                                    (cur_data_de = last_data_de.parent().nextElementSibling().nextElementSibling().children().first()) != null
                                ) || (
                                    last_data_de.parent().nextElementSibling() != null &&
                                    (cur_data_de = last_data_de.parent().nextElementSibling().children().first()) != null
                                )
                            )
                        )
                    );

                    retList.add(card);
                }

            }
        });
        return retList;
    }

    public List<CardRaw> request (List<CardRequest> requests) throws IOException {
        ArrayList<CardRaw> ret = new ArrayList<CardRaw>();
        for (CardRequest request: requests) {
            try {
                ret.addAll(request(request));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ret;
    }
}
