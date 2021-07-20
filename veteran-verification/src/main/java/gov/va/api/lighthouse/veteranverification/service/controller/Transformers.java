package gov.va.api.lighthouse.veteranverification.service.controller;

import static org.springframework.util.CollectionUtils.isEmpty;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.xml.datatype.XMLGregorianCalendar;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

/** Utility class for common transformer methods. */
@UtilityClass
public final class Transformers {
  /** Filter null items and return null if the result is null or empty. */
  public static <T> List<T> emptyToNull(List<T> items) {
    if (isEmpty(items)) {
      return null;
    }
    List<T> filtered = items.stream().filter(Objects::nonNull).collect(Collectors.toList());
    return filtered.isEmpty() ? null : filtered;
  }

  /** Return true if the value is a blank string, or any other object that is null. */
  public static boolean isBlank(Object value) {
    if (value instanceof CharSequence) {
      return StringUtils.isBlank((CharSequence) value);
    }
    if (value instanceof Collection<?>) {
      return ((Collection<?>) value).isEmpty();
    }
    if (value instanceof Optional<?>) {
      return ((Optional<?>) value).isEmpty() || isBlank(((Optional<?>) value).get());
    }
    if (value instanceof Map<?, ?>) {
      return ((Map<?, ?>) value).isEmpty();
    }
    return value == null;
  }

  /** Converts XmlGregorian to LocalDate. */
  public static LocalDate xmlGregorianToLocalDate(XMLGregorianCalendar calendar) {
    return calendar != null
        ? LocalDate.of(calendar.getYear(), calendar.getMonth(), calendar.getDay())
        : null;
  }
}
